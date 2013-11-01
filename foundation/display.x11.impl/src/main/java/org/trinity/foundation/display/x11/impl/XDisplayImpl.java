/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.display.x11.impl;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_window_attributes_cookie_t;
import org.freedesktop.xcb.xcb_get_window_attributes_reply_t;
import org.freedesktop.xcb.xcb_query_tree_cookie_t;
import org.freedesktop.xcb.xcb_query_tree_reply_t;
import org.freedesktop.xcb.xcb_screen_iterator_t;
import org.freedesktop.xcb.xcb_screen_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.Screen;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XScreen;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.api.XcbErrorUtil;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.*;
import static org.freedesktop.xcb.xcb_map_state_t.XCB_MAP_STATE_VIEWABLE;

@Bind
@Singleton
@ThreadSafe
@ExecutionContext(DisplayExecutor.class)
public class XDisplayImpl implements Display {

	private static final Logger LOG = LoggerFactory.getLogger(XDisplayImpl.class);
	private static final int CLIENT_EVENT_MASK = XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW
			| XCB_EVENT_MASK_STRUCTURE_NOTIFY;
	private static final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder())
			.putInt(CLIENT_EVENT_MASK);
	private final List<DisplaySurface> clientDisplaySurfaces = new ArrayList<>();
	private final XConnection xConnection;
	private final XWindowPoolImpl xWindowCache;
	private final ListeningExecutorService xExecutor;
	private final AsyncListenableEventBus displayEventBus;
	private final ByteBuffer rootWindowAttributes = allocateDirect(4).order(nativeOrder())
			.putInt(XCB_EVENT_MASK_PROPERTY_CHANGE | XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT);
	private XScreen screen;

	@Inject
	XDisplayImpl(	final XConnection xConnection,
					final XWindowPoolImpl xWindowCache,
					@DisplayExecutor final ListeningExecutorService xExecutor) throws ExecutionException,
			InterruptedException {
		this.xWindowCache = xWindowCache;
		this.xConnection = xConnection;
		this.xExecutor = xExecutor;
		this.displayEventBus = new AsyncListenableEventBus(this.xExecutor);
		// register to ourself so we can track newly created clients in the
		// "Display" thread. See onCreationNotify(...).
        this.displayEventBus.register(this);
        open();
	}

	@Override
	public ListenableFuture<Void> quit() {
        return this.xExecutor.submit(() -> {
            XDisplayImpl.this.xConnection.close();
            return null;
        });
    }

    private ListenableFuture<Void> open() {
        return this.xExecutor.submit(() -> {
            if(xcb_connection_has_error(XDisplayImpl.this.xConnection.getConnectionReference()) != 0) {
                throw new Error("Cannot open display\n");
            }
            // FIXME from config?
            final int targetScreen = 0;
            final xcb_screen_iterator_t iter = xcb_setup_roots_iterator(xcb_get_setup(XDisplayImpl.this.xConnection
                                                                                              .getConnectionReference()));
            int screenNr;
            for(; iter.getRem() != 0; --screenNr, xcb_screen_next(iter)) {
                if(targetScreen == 0) {
                    final xcb_screen_t xcb_screen = iter.getData();
                    configureRootEvents(xcb_screen);
                    XDisplayImpl.this.screen = new XScreenImpl(xcb_screen);
                    break;
                }
            }

            findClientDisplaySurfaces();
            return null;
        });
    }

    private void configureRootEvents(final xcb_screen_t xcb_screen) {
        final int rootId = xcb_screen.getRoot();
		xcb_change_window_attributes(	this.xConnection.getConnectionReference(),
										rootId,
										XCB_CW_EVENT_MASK,
                                        rootWindowAttributes);
		xcb_flush(this.xConnection.getConnectionReference());
	}

	private void findClientDisplaySurfaces() {
		// find client display surfaces that are already
		// active on the X server and track them

		final int root = screen.getScreenReference().getRoot();
		final SWIGTYPE_p_xcb_connection_t connection = this.xConnection.getConnectionReference();
		final xcb_query_tree_cookie_t query_tree_cookie = xcb_query_tree(	connection,
																			root);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		// this is a one time call, no need to make it
		// async.
		final xcb_query_tree_reply_t query_tree_reply = xcb_query_tree_reply(	connection,
																				query_tree_cookie,
																				e);
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			XDisplayImpl.LOG.error(	"X error while doing query tree: {}.",
									XcbErrorUtil.toString(e));
			return;
		}

		final ByteBuffer tree_children = xcb_query_tree_children(query_tree_reply).order(nativeOrder());
		int tree_children_length = xcb_query_tree_children_length(query_tree_reply);
		while (tree_children_length > 0) {
            tree_children_length--;
			final int tree_child = tree_children.getInt();

			final xcb_get_window_attributes_cookie_t get_window_attributes_cookie = xcb_get_window_attributes(	connection,
																												tree_child);
			final xcb_get_window_attributes_reply_t get_window_attributes_reply = xcb_get_window_attributes_reply(	connection,
																													get_window_attributes_cookie,
																													e);
			if (xcb_generic_error_t.getCPtr(e) != 0) {
				XDisplayImpl.LOG.error(	"X error while doing get window attributes: {}.",
										XcbErrorUtil.toString(e));
			} else {
				final short override_redirect = get_window_attributes_reply.getOverride_redirect();
				final short map_state = get_window_attributes_reply.getMap_state();
				// Check for override redirect flag and ignore the window if
				// it's set. Ignore unmapped windows, we'll see them as soon as
				// they reconfigure/map themselves
				if ((map_state != XCB_MAP_STATE_VIEWABLE) || (override_redirect != 0)) {
					continue;
				}

				final DisplaySurface clientWindow = this.xWindowCache.getDisplaySurface(new XWindowHandle(tree_child));
				configureClientEvents(clientWindow);
				trackClient(clientWindow);
			}
		}
	}

	private void configureClientEvents(final DisplaySurface window) {
		final int winId = (Integer) window.getDisplaySurfaceHandle().getNativeHandle();
		LOG.debug(	"[winId={}] configure client evens.",
					winId);
		xcb_change_window_attributes(	this.xConnection.getConnectionReference(),
										winId,
										XCB_CW_EVENT_MASK,
										CLIENT_EVENTS_CONFIG_BUFFER);
		xcb_flush(this.xConnection.getConnectionReference());
	}

	@Override
	public void register(@Nonnull final Object listener) {
		this.displayEventBus.scheduleRegister(listener);
	}

	@Override
	public void register(	@Nonnull final Object listener,
							@Nonnull final ExecutorService executor) {
		this.displayEventBus.register(	listener,
										executor);
	}

	@Override
	public void scheduleRegister(@Nonnull final Object listener) {
		this.displayEventBus.scheduleRegister(listener);
	}

	@Override
	public void scheduleRegister(	@Nonnull final Object listener,
									@Nonnull final ExecutorService listenerActivationExecutor) {
		this.displayEventBus.scheduleRegister(	listener,
												listenerActivationExecutor);
	}

	@Override
	public void post(@Nonnull final Object event) {
		this.displayEventBus.post(event);
	}

	@Subscribe
	public void onCreationNotify(final DisplaySurfaceCreationNotify displaySurfaceCreationNotify) {
        // keep track of all created clients so others can query them later.
        trackClient(displaySurfaceCreationNotify.getDisplaySurface());
	}

	private void trackClient(final DisplaySurface clientDisplaySurface) {
		clientDisplaySurface.register(new Object() {
			@SuppressWarnings("UnusedParameters")
            @Subscribe
			public void handleClientDestroyed(final DestroyNotify destroyNotify) {
				XDisplayImpl.this.clientDisplaySurfaces.remove(clientDisplaySurface);
			}
		});
		this.clientDisplaySurfaces.add(clientDisplaySurface);
	}

	@Override
	public void unregister(@Nonnull final Object listener) {
		this.displayEventBus.unregister(listener);
	}

	@Override
	public ListenableFuture<List<DisplaySurface>> getDisplaySurfaces() {
        return this.xExecutor
                .submit((Callable<List<DisplaySurface>>) () -> new ArrayList<>(XDisplayImpl.this.clientDisplaySurfaces));
    }

    @Override
	public ListenableFuture<Screen> getScreen() {
        return this.xExecutor.submit((Callable<Screen>) this::getScreenImpl);
    }

    protected Screen getScreenImpl() {
        return this.screen;
    }
}
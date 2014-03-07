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

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_get_window_attributes_cookie_t;
import org.freedesktop.xcb.xcb_get_window_attributes_reply_t;
import org.freedesktop.xcb.xcb_query_tree_cookie_t;
import org.freedesktop.xcb.xcb_query_tree_reply_t;
import org.freedesktop.xcb.xcb_screen_iterator_t;
import org.freedesktop.xcb.xcb_screen_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.shared.ListenableEventBus;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT;
import static org.freedesktop.xcb.xcb_map_state_t.XCB_MAP_STATE_VIEWABLE;

@Singleton
@NotThreadSafe
public class XEventChannel extends ListenableEventBus {

	private static final Logger LOG = LoggerFactory.getLogger(XEventChannel.class);

	private final ExecutorService eventPumpThread = Executors.newSingleThreadExecutor();
	private final ByteBuffer rootWindowAttributes = allocateDirect(4).order(nativeOrder())
																	 .putInt(XCB_EVENT_MASK_PROPERTY_CHANGE | XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT);
	private final XCompositor xCompositor;

	private SWIGTYPE_p_xcb_connection_t xcb_connection;

	@Inject
    XEventChannel(final XEventHandlers xEventHandlers,
                  final XCompositor xCompositor) {
		this.xCompositor = xCompositor;

		// FIXME from config?
		final String displayName = System.getenv("DISPLAY");
		final int targetScreen = 0;

		register(xEventHandlers);
		open(displayName,
			 targetScreen);
	}

	private void open(@Nonnull final String displayName,
					  @Nonnegative final Integer screen) {
		checkNotNull(displayName);

		final ByteBuffer screenBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		screenBuf.putInt(screen);
		this.xcb_connection = xcb_connect(displayName,
										  screenBuf);
		if(xcb_connection_has_error(getConnectionReference()) != 0) {
			throw new Error("Cannot open display\n");
		}
		// FIXME from config?
		final int targetScreen = 0;

		final xcb_screen_iterator_t iter = xcb_setup_roots_iterator(xcb_get_setup(getConnectionReference()));
		int screenNr;
		for(; iter.getRem() != 0; --screenNr, xcb_screen_next(iter)) {
			if(targetScreen == 0) {
				final xcb_screen_t xcb_screen = iter.getData();
				configureRootEvents(xcb_screen);
				findClientDisplaySurfaces(xcb_screen);
				break;
			}
		}

		pump();
	}

	private void configureRootEvents(final xcb_screen_t xcb_screen) {
		final int rootId = xcb_screen.getRoot();

		xcb_change_window_attributes(getConnectionReference(),
									 rootId,
									 XCB_CW_EVENT_MASK,
									 this.rootWindowAttributes);
		xcb_flush(getConnectionReference());
	}

	public void close() {
		xcb_disconnect(this.xcb_connection);
	}

	public SWIGTYPE_p_xcb_connection_t getConnectionReference() {
		return this.xcb_connection;
	}

	private void pump() {
		this.eventPumpThread.submit(new Runnable() {
			@Override
			public void run() {
				if(xcb_connection_has_error(getConnectionReference()) != 0) {
					final String errorMsg = "X11 connection was closed unexpectedly - maybe your X server terminated / crashed?";
					LOG.error(errorMsg);
					throw new Error(errorMsg);
				}

				final xcb_generic_event_t xcb_generic_event = xcb_wait_for_event(getConnectionReference());

				post(xcb_generic_event);
				xcb_generic_event.delete();

				pump();
			}
		});
	}

	private void findClientDisplaySurfaces(final xcb_screen_t screen) {
		// find client display surfaces that are already
		// active on the X server and track them

		final int root = screen.getRoot();
		final SWIGTYPE_p_xcb_connection_t connection = getConnectionReference();
		final xcb_query_tree_cookie_t query_tree_cookie = xcb_query_tree(connection,
																		 root);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		// this is a one time call, no need to make it
		// async.
		final xcb_query_tree_reply_t query_tree_reply = xcb_query_tree_reply(connection,
																			 query_tree_cookie,
																			 e);
		if(xcb_generic_error_t.getCPtr(e) != 0) {
			LOG.error("X error while doing query tree: {}.",
					  XcbErrorUtil.toString(e));
			return;
		}

		final ByteBuffer tree_children = xcb_query_tree_children(query_tree_reply).order(nativeOrder());
		int tree_children_length = xcb_query_tree_children_length(query_tree_reply);
		while(tree_children_length > 0) {
			tree_children_length--;

			final int tree_child = tree_children.getInt();

			final xcb_get_window_attributes_cookie_t get_window_attributes_cookie = xcb_get_window_attributes(connection,
																											  tree_child);

			final xcb_get_window_attributes_reply_t get_window_attributes_reply = xcb_get_window_attributes_reply(connection,
																												  get_window_attributes_cookie,
																												  e);

			if(xcb_generic_error_t.getCPtr(e) != 0) {
				LOG.error("X error while doing get window attributes: {}.",
						  XcbErrorUtil.toString(e));
			}
			else {
				final short override_redirect = get_window_attributes_reply.getOverride_redirect();
				final short map_state = get_window_attributes_reply.getMap_state();
				// Check for override redirect flag and ignore the window if
				// it's set. Ignore unmapped displaySurfaces, we'll see them as soon as
				// they reconfigure/map themselves
				if((map_state != XCB_MAP_STATE_VIEWABLE) || (override_redirect != 0)) {
					continue;
				}

				this.xCompositor.getDisplaySurface(XWindowHandle.create(tree_child));
			}
		}
	}
}

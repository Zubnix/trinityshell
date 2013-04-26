/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.x11.impl;

import static com.google.common.util.concurrent.Futures.addCallback;
import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_query_tree;
import static org.freedesktop.xcb.LibXcb.xcb_query_tree_children;
import static org.freedesktop.xcb.LibXcb.xcb_query_tree_children_length;
import static org.freedesktop.xcb.LibXcb.xcb_query_tree_reply;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import javax.annotation.concurrent.ThreadSafe;

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_query_tree_cookie_t;
import org.freedesktop.xcb.xcb_query_tree_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.CreationNotify;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.foundation.api.shared.OwnerThread;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
@ThreadSafe
@OwnerThread("Display")
public class XDisplayServer implements DisplayServer {

	private static final Logger logger = LoggerFactory
			.getLogger(XDisplayServer.class);

	private final List<DisplaySurface> clientDisplaySurfaces = new ArrayList<DisplaySurface>();

	private final XConnection xConnection;
	private final XWindowCache xWindowCache;
	private final XEventPump xEventPump;
	private final ListeningExecutorService xExecutor;

	private final AsyncListenableEventBus displayEventBus;

	@Inject
	XDisplayServer(	final XConnection xConnection,
					final XWindowCache xWindowCache,
					final XEventPump xEventPump,
					@Named("Display") final ListeningExecutorService xExecutor) {

		this.xWindowCache = xWindowCache;
		this.xConnection = xConnection;
		this.xEventPump = xEventPump;
		this.xExecutor = xExecutor;
		this.displayEventBus = new AsyncListenableEventBus(this.xExecutor);

		open();
	}

	@Override
	public ListenableFuture<Void> quit() {

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												XDisplayServer.this.xEventPump
														.stop();
												XDisplayServer.this.xConnection
														.close();
											}
										},
										null);
	}

	@Override
	public ListenableFuture<DisplaySurface> getRootDisplayArea() {
		return this.xExecutor.submit(new Callable<DisplaySurface>() {
			@Override
			public DisplaySurface call() {
				final int rootWinId = XDisplayServer.this.xConnection
						.getScreenReference().getRoot();
				return XDisplayServer.this.xWindowCache.getWindow(rootWinId);
			}
		});
	}

	public ListenableFuture<Void> open() {
		// FIXME from config?
		final String displayName = System.getenv("DISPLAY");

		return this.xExecutor.submit(	new Runnable() {
											@Override
											public void run() {
												XDisplayServer.this.xConnection
														.open(	displayName,
																0);
												if (xcb_connection_has_error(XDisplayServer.this.xConnection
														.getConnectionReference()) != 0) {
													throw new Error("Cannot open display\n");
												}

												findClientDisplaySurfaces();
												XDisplayServer.this.xEventPump
														.start();
											}
										},
										null);
	}

	private void findClientDisplaySurfaces() {
		final ListenableFuture<DisplaySurface> rootDisplayAreaFuture = getRootDisplayArea();
		addCallback(rootDisplayAreaFuture,
					new FutureCallback<DisplaySurface>() {
						@Override
						public void onSuccess(final DisplaySurface root) {
							// find client display surfaces that are already
							// active on the X server and track them
							final int rootId = (Integer) root
									.getDisplaySurfaceHandle()
									.getNativeHandle();
							final SWIGTYPE_p_xcb_connection_t xConnectionRef = XDisplayServer.this.xConnection
									.getConnectionReference();
							final xcb_query_tree_cookie_t query_tree_cookie_t = xcb_query_tree(	xConnectionRef,
																								rootId);
							// this is a one time call, no need to make it
							// async.

							final xcb_generic_error_t e = new xcb_generic_error_t();

							final xcb_query_tree_reply_t query_tree_reply = xcb_query_tree_reply(	xConnectionRef,
																									query_tree_cookie_t,
																									e);
							if (xcb_generic_error_t.getCPtr(e) != 0) {
								XDisplayServer.logger
										.error(	"X error: {}.",
												XcbErrorUtil.toString(e));
								return;
							}

							handleQueryTreeReply(query_tree_reply);
						}

						@Override
						public void onFailure(final Throwable t) {
							XDisplayServer.logger
									.error(	"Error while querying root display surface.",
											t);
						}
					});
	}

	private void
			handleQueryTreeReply(final xcb_query_tree_reply_t query_tree_reply) {
		final ByteBuffer tree_children = xcb_query_tree_children(query_tree_reply);
		int tree_children_length = xcb_query_tree_children_length(query_tree_reply);
		while (tree_children_length > 0) {
			final int clientWindowId = tree_children.getInt();
			// TODO we should check for override redirect flag and ignore the
			// window if it's set.

			final XWindow clientWindow = this.xWindowCache
					.getWindow(clientWindowId);
			trackClient(clientWindow);

			tree_children_length--;
		}
	}

	@Override
	public void register(final Object listener) {
		this.displayEventBus.register(listener);
	}

	@Override
	public void register(	final Object listener,
							final ExecutorService executor) {
		this.displayEventBus.register(	listener,
										executor);

	}

	@Override
	public void post(final Object event) {
		if (event instanceof CreationNotify) {
			// keep track of all created clients so others can query them later.
			final CreationNotify clientCreationNotify = (CreationNotify) event;
			trackClient(clientCreationNotify.getDisplaySurface());
		}
		this.displayEventBus.post(event);
	}

	private void trackClient(final DisplaySurface clientDisplaySurface) {
		clientDisplaySurface.register(new Object() {
			@Subscribe
			public void
					handleClientDestroyed(final DestroyNotify destroyNotify) {
				XDisplayServer.this.clientDisplaySurfaces
						.remove(clientDisplaySurface);
			}
		});
		this.clientDisplaySurfaces.add(clientDisplaySurface);
	}

	@Override
	public void unregister(final Object listener) {
		this.displayEventBus.unregister(listener);
	}

	@Override
	public ListenableFuture<DisplaySurface[]> getClientDisplaySurfaces() {

		return this.xExecutor.submit(new Callable<DisplaySurface[]>() {
			@Override
			public DisplaySurface[] call() throws Exception {
				// we return a copy
				return XDisplayServer.this.clientDisplaySurfaces
						.toArray(new DisplaySurface[] {});
			}
		});
	}
}
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

import java.util.concurrent.Callable;

import javax.annotation.concurrent.ThreadSafe;

import org.freedesktop.xcb.LibXcb;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
@ThreadSafe
public class XDisplayServer implements DisplayServer {

	private final XConnection xConnection;
	private final XWindowCache xWindowCache;
	private final XEventPump xEventPump;
	private final ListeningExecutorService xExecutor;

	private final EventBus displayEventBus = new EventBus();

	@Inject
	XDisplayServer(	final XConnection xConnection,
					final XWindowCache xWindowCache,
					@Named("DisplayEventBus") final EventBus displayEventBus,
					final XEventPump xEventPump,
					@Named("XExecutor") final ListeningExecutorService xExecutor) {

		this.xWindowCache = xWindowCache;
		this.xConnection = xConnection;
		this.xEventPump = xEventPump;
		this.xExecutor = xExecutor;
	}

	@Override
	public ListenableFuture<Void> close() {

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												XDisplayServer.this.xEventPump.stop();
												XDisplayServer.this.xConnection.close();
											}
										},
										null);
	}

	@Override
	public ListenableFuture<DisplaySurface> getRootDisplayArea() {
		return this.xExecutor.submit(new Callable<DisplaySurface>() {

			@Override
			public DisplaySurface call() {
				return XDisplayServer.this.xWindowCache.getWindow(XDisplayServer.this.xConnection.getScreenReference()
						.getRoot());
			}
		});
	}

	@Override
	public ListenableFuture<Void> open() {
		// FIXME from config
		final String displayName = System.getenv("DISPLAY");

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												XDisplayServer.this.xConnection.open(	displayName,
																						0);
												if (LibXcb.xcb_connection_has_error(XDisplayServer.this.xConnection
														.getConnectionReference()) != 0) {
													throw new Error("Cannot open display\n");
												}
												XDisplayServer.this.xEventPump.start();

											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> addListener(final Object listener) {
		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												XDisplayServer.this.displayEventBus.register(listener);
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> post(final Object event) {
		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												XDisplayServer.this.displayEventBus.post(event);
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> removeListener(final Object listener) {
		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												XDisplayServer.this.displayEventBus.unregister(listener);
											}
										},
										null);
	}
}
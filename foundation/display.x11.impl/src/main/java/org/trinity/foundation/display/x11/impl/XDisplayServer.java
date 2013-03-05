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

import org.freedesktop.xcb.LibXcb;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DisplayEvent;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XDisplayServer implements DisplayServer, XEventTarget {

	private final XConnection xConnection;
	private final XWindowCache xWindowCache;
	private final XEventPump xEventPump;
	private final ListeningExecutorService xExecutor;

	private EventBus displayEventBus = new EventBus();

	@Inject
	XDisplayServer(final XConnection xConnection,
			final XWindowCache xWindowCache,
			@Named("DisplayEventBus") final EventBus displayEventBus,
			XEventPump xEventPump,
			@Named("XExecutor") ListeningExecutorService xExecutor) {

		this.xWindowCache = xWindowCache;
		this.xConnection = xConnection;
		this.xEventPump = xEventPump;
		this.xExecutor = xExecutor;
	}

	@Override
	public ListenableFuture<Void> close() {

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				xEventPump.stop();
				xConnection.close();
			}
		}, null);
	}

	@Override
	public ListenableFuture<DisplaySurface> getRootDisplayArea() {
		return xExecutor.submit(new Callable<DisplaySurface>() {

			@Override
			public DisplaySurface call() {
				return xWindowCache.getWindow(xConnection.getScreenReference()
						.getRoot());
			}
		});
	}

	@Override
	public ListenableFuture<Void> open() {
		// FIXME from config
		final String displayName = System.getenv("DISPLAY");

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				xConnection.open(displayName, 0);
				if (LibXcb.xcb_connection_has_error(xConnection
						.getConnectionReference()) != 0) {
					throw new Error("Cannot open display\n");
				}
				xEventPump.start();

			}
		}, null);
	}

	@Override
	public void addListener(Object listener) {
		this.displayEventBus.register(listener);
	}

	@Override
	public void post(DisplayEvent displayEvent) {
		this.displayEventBus.post(displayEvent);
	}

	@Override
	public void removeListener(Object listener) {
		this.displayEventBus.unregister(listener);
	}
}
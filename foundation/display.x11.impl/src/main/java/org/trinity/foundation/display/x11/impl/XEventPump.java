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

import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_wait_for_event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;
import org.trinity.foundation.display.x11.api.XConnection;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@NotThreadSafe
public class XEventPump implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(XEventPump.class);

	private final XConnection connection;
	private final EventBus xEventBus;
	private final ExecutorService xEventPumpExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {

		@Override
		public Thread newThread(final Runnable r) {
			return new Thread(	r,
								"x-event-pump");
		}
	});
	private final ListeningExecutorService xExecutor;

	@Inject
	XEventPump(	final XConnection connection,
				@Named("XEventBus") final EventBus xEventBus,
				@Named("Display") final ListeningExecutorService xExecutor) {
		this.connection = connection;
		this.xEventBus = xEventBus;
		this.xExecutor = xExecutor;
	}

	@Override
	public void run() {
		final xcb_generic_event_t event = xcb_wait_for_event(this.connection.getConnectionReference());

		if (xcb_connection_has_error(this.connection.getConnectionReference()) != 0) {
			final String errorMsg = "X11 connection was closed unexpectedly - maybe your X server terminated / crashed?";
			XEventPump.logger.error(errorMsg);
			throw new Error(errorMsg);
		}

		// pass x event from x-event-pump thread to x-executor thread.
		this.xExecutor.submit(new Runnable() {
			@Override
			public void run() {
				XEventPump.this.xEventBus.post(event);
				event.delete();
			}
		});

		// schedule next event retrieval
		this.xEventPumpExecutor.submit(this);
	}

	public void start() {
		this.xEventPumpExecutor.submit(this);
	}

	public void stop() {
		this.xEventPumpExecutor.shutdown();
		try {
			if (this.xEventPumpExecutor.awaitTermination(	10,
															TimeUnit.SECONDS)) {
				return;
			}
			XEventPump.logger.error("X event pump could not terminate gracefully!");
		} catch (final InterruptedException e) {
			XEventPump.logger.error("X event pump terminate was interrupted.",
									e);
		}
	}
}

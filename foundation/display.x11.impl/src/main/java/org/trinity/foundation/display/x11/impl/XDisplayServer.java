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

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.freedesktop.xcb.LibXcb;
import org.trinity.foundation.api.display.DisplayEventProducer;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DisplayEvent;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XDisplayServer implements DisplayServer {

	private static final int QUEUE_SIZE = 32;
	private final ArrayBlockingQueue<DisplayEvent> displayEvents = new ArrayBlockingQueue<DisplayEvent>(XDisplayServer.QUEUE_SIZE);

	private final XConnection xConnection;
	private final XWindowCache xWindowCache;
	private final Set<DisplayEventProducer> displayEventProducers;

	@Inject
	XDisplayServer(	final XConnection xConnection,
					final XWindowCache xWindowCache,
					@Named("DisplayEventBus") final EventBus displayEventBus,
					final Set<DisplayEventProducer> displayEventProducers) {

		displayEventBus.register(this);
		this.xWindowCache = xWindowCache;
		this.xConnection = xConnection;
		this.displayEventProducers = displayEventProducers;

		// FIXME from config
		final String displayName = System.getenv("DISPLAY");
		this.xConnection.open(	displayName,
								0);
		if (LibXcb.xcb_connection_has_error(this.xConnection.getConnectionReference()) != 0) {
			throw new Error("Cannot open display\n");
		}
		startUp();
	}

	@Subscribe
	@AllowConcurrentEvents
	public void handleDisplayEvent(final DisplayEvent displayEvent) {
		try {
			if (displayEvent == null) {
				return;
			}
			this.displayEvents.put(displayEvent);
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasNextDisplayEvent() {
		return this.displayEvents.size() != 0;
	}

	@Override
	public DisplayEvent getNextDisplayEvent() {
		try {
			LibXcb.xcb_flush(this.xConnection.getConnectionReference());
			return this.displayEvents.take();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void shutDown() {
		for (final DisplayEventProducer displayEventProducer : this.displayEventProducers) {
			displayEventProducer.stopDisplayEventProduction();
		}
		this.xConnection.close();
		this.displayEvents.clear();
	}

	@Override
	public DisplaySurface getRootDisplayArea() {
		return this.xWindowCache.getWindow(this.xConnection.getScreenReference().getRoot());
	}

	public void startUp() {
		for (final DisplayEventProducer displayEventProducer : this.displayEventProducers) {
			displayEventProducer.startDisplayEventProduction();
		}
	}
}

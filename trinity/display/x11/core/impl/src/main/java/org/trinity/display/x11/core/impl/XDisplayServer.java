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
package org.trinity.display.x11.core.impl;

import java.util.concurrent.ArrayBlockingQueue;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.event.DisplayEvent;

import xcbjb.xcb_generic_event_t;

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
	private final ArrayBlockingQueue<DisplayEvent> displayEvents = new ArrayBlockingQueue<DisplayEvent>(QUEUE_SIZE);

	private int time;

	@Inject
	private XConnection xConnection;
	@Inject
	@Named("xEventBus")
	private EventBus xEventBus;
	@Inject
	private DisplayEventConverter displayEventConverter;

	public XDisplayServer() {
		this.time = 0;
		this.xEventBus.register(this);
		// FIXME from configuration
		final String displayName = System.getenv("DISPLAY");
		this.xConnection.open(displayName, 0);
	}

	@Override
	public boolean hasNextDisplayEvent() {
		return this.displayEvents.size() != 0;
	}

	@Override
	public DisplayEvent getNextDisplayEvent() {
		try {
			this.displayEvents.take();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Subscribe
	public void handleXEvent(final xcb_generic_event_t event_t) {
		final DisplayEvent displayEvent = this.displayEventConverter
				.convert(event_t);
		if (displayEvent != null) {
			this.displayEvents.add(displayEvent);
		}
	}

	@Override
	public void shutDown() {
		this.xConnection.close();
		this.displayEvents.clear();
	}

	public int getTime() {
		return this.time;
	}

	public void setTime(final int time) {
		this.time = time;
	}

}

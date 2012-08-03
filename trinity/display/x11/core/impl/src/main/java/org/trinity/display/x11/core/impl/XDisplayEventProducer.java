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

import org.trinity.foundation.display.api.DisplayEventProducer;

import xcbjb.LibXcb;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;

@Bind(multiple = true)
@To(customs = DisplayEventProducer.class)
public class XDisplayEventProducer implements DisplayEventProducer, Runnable {

	@Inject
	private XConnection connection;
	@Inject
	private DisplayEventConverter displayEventConverter;
	@Inject
	@Named("xEventBus")
	private EventBus xEventBus;

	private Thread producerThread;

	@Override
	public void start() {
		this.producerThread = new Thread(this);
	}

	@Override
	public void stop() {
		this.producerThread.interrupt();
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			final xcb_generic_event_t event_t = LibXcb
					.xcb_wait_for_event(this.connection
							.getConnectionReference());
			this.xEventBus.post(event_t);
			Thread.yield();
		}
	}
}

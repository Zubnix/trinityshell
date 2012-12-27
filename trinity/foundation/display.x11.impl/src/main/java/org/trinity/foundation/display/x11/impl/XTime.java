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

import xcb.xcb_button_press_event_t;
import xcb.xcb_enter_notify_event_t;
import xcb.xcb_key_press_event_t;
import xcb.xcb_property_notify_event_t;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XTime {

	private volatile int time = 0;

	@Inject
	XTime(@Named("xEventBus") final EventBus xEventBus) {
		xEventBus.register(this);
	}

	public int getTime() {
		return this.time;
	}

	@Subscribe
	public void handleButtonPressed(final xcb_button_press_event_t press_event_t) {
		// press&release have the same type
		this.time = press_event_t.getTime();
	}

	@Subscribe
	public void handleKeyPressed(final xcb_key_press_event_t key_press_event_t) {
		// press&release have the same type
		this.time = key_press_event_t.getTime();
	}

	@Subscribe
	public void handlePropertyNotify(final xcb_property_notify_event_t property_notify_event_t) {
		this.time = property_notify_event_t.getTime();
	}

	@Subscribe
	public void handleEnterNotify(final xcb_enter_notify_event_t enter_notify_event_t) {
		// enter & leave have the same type
		this.time = enter_notify_event_t.getTime();
	}
}

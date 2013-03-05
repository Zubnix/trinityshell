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

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.concurrent.ThreadSafe;

import org.freedesktop.xcb.xcb_button_press_event_t;
import org.freedesktop.xcb.xcb_enter_notify_event_t;
import org.freedesktop.xcb.xcb_key_press_event_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;

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
@ThreadSafe
public class XTime {

	private final AtomicInteger time = new AtomicInteger();

	@Inject
	XTime(@Named("XEventBus") final EventBus xEventBus) {
		xEventBus.register(this);
	}

	public int getTime() {
		return this.time.get();
	}

	@Subscribe
	public void handleButtonPressed(final xcb_button_press_event_t press_event_t) {
		// press&release have the same type
		this.time.set(press_event_t.getTime());
	}

	@Subscribe
	public void handleKeyPressed(final xcb_key_press_event_t key_press_event_t) {
		// press&release have the same type
		this.time.set(key_press_event_t.getTime());
	}

	@Subscribe
	public void handlePropertyNotify(final xcb_property_notify_event_t property_notify_event_t) {
		this.time.set(property_notify_event_t.getTime());
	}

	@Subscribe
	public void handleEnterNotify(final xcb_enter_notify_event_t enter_notify_event_t) {
		// enter & leave have the same type
		this.time.set(enter_notify_event_t.getTime());
	}
}

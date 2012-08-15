package org.trinity.display.x11.core.impl;

import xcbjb.xcb_button_press_event_t;
import xcbjb.xcb_enter_notify_event_t;
import xcbjb.xcb_key_press_event_t;
import xcbjb.xcb_property_notify_event_t;

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

package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.Momentum;

import xcbjb.LibXcb;
import xcbjb.xcb_generic_event_t;
import xcbjb.xcb_key_press_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class KeyPressConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_KEY_PRESS);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	KeyPressConversion(final EventBus xEventBus, final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_key_press_event_t key_press_event_t = new xcb_key_press_event_t(	xcb_generic_event_t
																							.getCPtr(event_t),
																					true);
		this.xEventBus.post(key_press_event_t);

		final int windowId = key_press_event_t.getEvent();
		final XWindow displayEventSource = this.xWindowCache
				.getWindow(windowId);

		final int keyCode = key_press_event_t.getDetail();
		final Key key = new Key(keyCode);

		final InputModifiers inputModifiers = new InputModifiers(key_press_event_t
				.getState());

		final KeyboardInput input = new KeyboardInput(	Momentum.STARTED,
														key,
														inputModifiers);

		final DisplayEvent displayEvent = new KeyNotifyEvent(	displayEventSource,
																input);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}
}
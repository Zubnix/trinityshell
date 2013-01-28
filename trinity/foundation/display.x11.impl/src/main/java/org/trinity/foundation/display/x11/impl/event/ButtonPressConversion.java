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
package org.trinity.foundation.display.x11.impl.event;

import org.trinity.foundation.api.display.event.ButtonNotifyEvent;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.input.Button;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.display.x11.impl.XEventConversion;
import org.trinity.foundation.display.x11.impl.XWindow;
import org.trinity.foundation.display.x11.impl.XWindowCache;

import xcb.LibXcbConstants;
import xcb.xcb_button_press_event_t;
import xcb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class ButtonPressConversion implements XEventConversion {

	private final XWindowCache windowCache;
	private final EventBus xEventBus;

	@Inject
	ButtonPressConversion(	final XWindowCache windowCache,
							@Named("XEventBus") final EventBus xEventBus) {
		this.windowCache = windowCache;
		this.xEventBus = xEventBus;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_button_press_event_t button_press_event_t = new xcb_button_press_event_t(	xcb_generic_event_t
																									.getCPtr(event_t),
																							true);

		// TODO logging
		System.err.println(String.format(	"Received %s",
											button_press_event_t.getClass()
													.getSimpleName()));

		this.xEventBus.post(button_press_event_t);

		final int windowId = button_press_event_t.getEvent();
		final XWindow window = this.windowCache.getWindow(windowId);

		final int buttonCode = button_press_event_t.getDetail();
		final Button button = new Button(buttonCode);
		final int modifiers = button_press_event_t.getState();
		final InputModifiers inputModifiers = new InputModifiers(modifiers);
		final int relativeX = button_press_event_t.getEvent_x();
		final int relativeY = button_press_event_t.getEvent_y();
		final int rootX = button_press_event_t.getRoot_x();
		final int rootY = button_press_event_t.getRoot_y();

		final PointerInput pointerInput = new PointerInput(	Momentum.STARTED,
															button,
															inputModifiers,
															relativeX,
															relativeY,
															rootX,
															rootY);

		final ButtonNotifyEvent buttonNotifyEvent = new ButtonNotifyEvent(	window,
																			pointerInput);
		return buttonNotifyEvent;
	}

	@Override
	public Integer getEventCode() {
		final int eventCode = LibXcbConstants.XCB_BUTTON_PRESS;
		return Integer.valueOf(eventCode);
	}
}
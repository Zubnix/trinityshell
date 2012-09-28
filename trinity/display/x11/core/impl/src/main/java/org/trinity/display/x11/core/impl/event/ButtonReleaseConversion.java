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
package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.input.api.PointerInput;

import xcbjb.LibXcbConstants;
import xcbjb.xcb_button_press_event_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class ButtonReleaseConversion implements XEventConversion {

	private final XWindowCache windowCache;
	private final EventBus xEventBus;

	@Inject
	ButtonReleaseConversion(final XWindowCache windowCache, @Named("xEventBus") final EventBus xEventBus) {
		this.windowCache = windowCache;
		this.xEventBus = xEventBus;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		// press has same structure as release
		final xcb_button_press_event_t button_release_event_t = new xcb_button_press_event_t(	xcb_generic_event_t.getCPtr(event_t),
																								true);
		// TODO logging
		System.err.println(String.format(	"Received %s",
											button_release_event_t.getClass().getSimpleName()));

		this.xEventBus.post(button_release_event_t);

		final int windowId = button_release_event_t.getEvent();
		final XWindow window = this.windowCache.getWindow(windowId);

		final int buttonCode = button_release_event_t.getDetail();
		final Button button = new Button(buttonCode);
		final int modifiers = button_release_event_t.getState();
		final InputModifiers inputModifiers = new InputModifiers(modifiers);
		final int relativeX = button_release_event_t.getEvent_x();
		final int relativeY = button_release_event_t.getEvent_y();
		final int rootX = button_release_event_t.getRoot_x();
		final int rootY = button_release_event_t.getRoot_y();

		final PointerInput pointerInput = new PointerInput(	Momentum.STOPPED,
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
		final int eventCode = LibXcbConstants.XCB_BUTTON_RELEASE;
		return Integer.valueOf(eventCode);
	}
}

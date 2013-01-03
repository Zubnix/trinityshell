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
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.Momentum;

import xcb.LibXcb;
import xcb.xcb_generic_event_t;
import xcb.xcb_key_press_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class KeyReleaseConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_KEY_RELEASE);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	KeyReleaseConversion(@Named("xEventBus") final EventBus xEventBus, final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		// press has same structure as release.
		final xcb_key_press_event_t key_release_event_t = new xcb_key_press_event_t(xcb_generic_event_t.getCPtr(event_t),
																					true);
		// TODO logging
		System.err.println(String.format(	"Received %s",
											key_release_event_t.getClass().getSimpleName()));

		this.xEventBus.post(key_release_event_t);

		final int windowId = key_release_event_t.getEvent();
		final XWindow displayEventSource = this.xWindowCache.getWindow(windowId);

		final int keyCode = key_release_event_t.getDetail();
		final Key key = new Key(keyCode);

		final int inputModifiersMask = key_release_event_t.getState();
		final InputModifiers inputModifiers = new InputModifiers(inputModifiersMask);

		final KeyboardInput input = new KeyboardInput(	Momentum.STOPPED,
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
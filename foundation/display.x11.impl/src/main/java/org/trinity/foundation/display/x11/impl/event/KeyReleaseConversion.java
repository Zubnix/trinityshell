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

import javax.annotation.concurrent.Immutable;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_key_press_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.KeyNotify;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.display.x11.impl.XEventConversion;
import org.trinity.foundation.display.x11.impl.XWindow;
import org.trinity.foundation.display.x11.impl.XWindowCache;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
@Immutable
public class KeyReleaseConversion implements XEventConversion {

	private static final Logger logger = LoggerFactory.getLogger(KeyReleaseConversion.class);

	private static final Integer eventCode = Integer.valueOf(LibXcb.XCB_KEY_RELEASE);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	KeyReleaseConversion(	@Named("XEventBus") final EventBus xEventBus,
							final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event) {

		// press has same structure as release.
		final xcb_key_press_event_t key_release_event = cast(event);

		logger.debug(	"Received X event={}",
						key_release_event.getClass().getSimpleName());

		this.xEventBus.post(key_release_event);
		final int keyCode = key_release_event.getDetail();
		final Key key = new Key(keyCode);
		final int inputModifiersMask = key_release_event.getState();

		final InputModifiers inputModifiers = new InputModifiers(inputModifiersMask);

		final KeyboardInput input = new KeyboardInput(	Momentum.STOPPED,
														key,
														inputModifiers);
		final DisplayEvent displayEvent = new KeyNotify(input);

		return displayEvent;
	}

	private xcb_key_press_event_t cast(final xcb_generic_event_t event) {
		return new xcb_key_press_event_t(	xcb_generic_event_t.getCPtr(event),
											false);
	}

	@Override
	public AsyncListenable getTarget(final xcb_generic_event_t event_t) {
		// press has same structure as release.
		final xcb_key_press_event_t key_release_event_t = cast(event_t);
		final int windowId = key_release_event_t.getEvent();
		final XWindow displayEventTarget = this.xWindowCache.getWindow(windowId);

		return displayEventTarget;
	}

	@Override
	public Integer getEventCode() {
		return eventCode;
	}
}
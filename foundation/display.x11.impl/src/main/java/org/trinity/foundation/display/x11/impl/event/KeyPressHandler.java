/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.display.x11.impl.event;

import static org.freedesktop.xcb.LibXcbConstants.XCB_KEY_PRESS;

import javax.annotation.concurrent.Immutable;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_key_press_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.KeyNotify;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XEventHandler;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;
import org.trinity.foundation.display.x11.impl.XWindowCacheImpl;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(multiple = true)
@Singleton
@ExecutionContext(DisplayExecutor.class)
@Immutable
public class KeyPressHandler implements XEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(KeyPressHandler.class);
	private static final Integer EVENT_CODE = XCB_KEY_PRESS;
	private final EventBus xEventBus;
	private final XWindowCacheImpl xWindowCache;

	@Inject
	KeyPressHandler(@XEventBus final EventBus xEventBus,
					final XWindowCacheImpl xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public Optional<KeyNotify> handle(final xcb_generic_event_t event) {
		final xcb_key_press_event_t key_press_event = cast(event);

		LOG.debug(	"Received X event={}",
					key_press_event.getClass().getSimpleName());

		this.xEventBus.post(key_press_event);

		final int keyCode = key_press_event.getDetail();
		final Key key = new Key(keyCode);

		final InputModifiers inputModifiers = new InputModifiers(key_press_event.getState());

		final KeyboardInput input = new KeyboardInput(	Momentum.STARTED,
														key,
														inputModifiers);

		return Optional.of(new KeyNotify(input));
	}

	private xcb_key_press_event_t cast(final xcb_generic_event_t event) {
		return new xcb_key_press_event_t(	xcb_generic_event_t.getCPtr(event),
											false);
	}

	@Override
	public Optional<DisplaySurface> getTarget(final xcb_generic_event_t event) {
		final xcb_key_press_event_t key_press_event_t = cast(event);
		final int windowId = key_press_event_t.getEvent();
		return Optional.of(this.xWindowCache.getWindow(windowId));
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}
}

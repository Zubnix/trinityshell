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

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.xcb_focus_in_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.FocusGainNotify;
import org.trinity.foundation.display.x11.api.XEventHandler;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePoolImpl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.inject.Singleton;

import static org.freedesktop.xcb.LibXcbConstants.XCB_FOCUS_IN;

@Singleton
@Immutable
public class FocusInHandler implements XEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(FocusInHandler.class);
	private static final Integer EVENT_CODE = XCB_FOCUS_IN;
	private final EventBus xEventBus;
	private final DisplaySurfacePoolImpl xWindowCache;

	@Inject
	FocusInHandler(	@XEventBus final EventBus xEventBus,
					final DisplaySurfacePoolImpl xWindowPool) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowPool;
	}

	@Override
	public Optional<FocusGainNotify> handle(@Nonnull final xcb_generic_event_t event_t) {

		final xcb_focus_in_event_t focus_in_event = cast(event_t);

		LOG.debug(	"Received X event={}",
					focus_in_event.getClass().getSimpleName());

		this.xEventBus.post(focus_in_event);

		return Optional.of(new FocusGainNotify());
	}

	private xcb_focus_in_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_focus_in_event_t(xcb_generic_event_t.getCPtr(event_t),
										false);
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}

	@Override
	public Optional<DisplaySurface> getTarget(@Nonnull final xcb_generic_event_t event_t) {
		final xcb_focus_in_event_t focus_in_event_t = cast(event_t);
		final int windowId = focus_in_event_t.getEvent();
		return Optional.of(this.xWindowCache.getDisplaySurface(new XWindowHandle(windowId)));
	}
}

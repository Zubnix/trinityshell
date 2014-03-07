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
import org.freedesktop.xcb.xcb_focus_in_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.FocusLostNotify;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePool;
import org.trinity.foundation.display.x11.impl.XEventChannel;
import org.trinity.foundation.display.x11.impl.XEventHandler;
import org.trinity.foundation.display.x11.impl.XWindowHandle;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_FOCUS_OUT;

@Immutable
public class FocusOutHandler implements XEventHandler {

	private static final Logger  LOG        = LoggerFactory.getLogger(FocusOutHandler.class);
	private static final Integer EVENT_CODE = XCB_FOCUS_OUT;
	private final XEventChannel      xEventChannel;
	private final DisplaySurfacePool xWindowCache;

	@Inject
	FocusOutHandler(final XEventChannel xEventChannel,
					final DisplaySurfacePool xWindowCache) {
		this.xEventChannel = xEventChannel;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public Optional<FocusLostNotify> handle(@Nonnull final xcb_generic_event_t event_t) {
		// focus in structure is the same as focus out.
		final xcb_focus_in_event_t focus_out_event = cast(event_t);

		LOG.debug("Received X event={}",
				  focus_out_event.getClass().getSimpleName());

		this.xEventChannel.post(focus_out_event);

		return Optional.of(new FocusLostNotify());
	}

	private xcb_focus_in_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_focus_in_event_t(xcb_generic_event_t.getCPtr(event_t),
										false);
	}

	@Override
	public Optional<DisplaySurface> getTarget(@Nonnull final xcb_generic_event_t event_t) {
		// focus in structure is the same as focus out.
		final xcb_focus_in_event_t focus_out_event_t = cast(event_t);

		return Optional.of(this.xWindowCache.get(XWindowHandle.create(focus_out_event_t.getEvent())));
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}
}

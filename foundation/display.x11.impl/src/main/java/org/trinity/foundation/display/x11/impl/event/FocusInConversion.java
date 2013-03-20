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
import org.freedesktop.xcb.xcb_focus_in_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.FocusGainNotify;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.display.x11.impl.XEventConversion;
import org.trinity.foundation.display.x11.impl.XWindow;
import org.trinity.foundation.display.x11.impl.XWindowCache;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
@Immutable
public class FocusInConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_FOCUS_IN);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	FocusInConversion(	final EventBus xEventBus,
						final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_focus_in_event_t focus_in_event_t = cast(event_t);

		// TODO logging
		System.err.println(String.format(	"Received %s",
											focus_in_event_t.getClass().getSimpleName()));

		this.xEventBus.post(focus_in_event_t);
		final DisplayEvent displayEvent = new FocusGainNotify();
		return displayEvent;
	}

	private xcb_focus_in_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_focus_in_event_t(xcb_generic_event_t.getCPtr(event_t),
										true);
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}

	@Override
	public AsyncListenable getTarget(final xcb_generic_event_t event_t) {
		final xcb_focus_in_event_t focus_in_event_t = cast(event_t);
		final int windowId = focus_in_event_t.getEvent();
		final XWindow displayEventTarget = this.xWindowCache.getWindow(windowId);
		return displayEventTarget;
	}
}
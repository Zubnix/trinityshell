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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.FocusLostNotify;
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
public class FocusOutConversion implements XEventConversion {

	private static final Logger logger = LoggerFactory.getLogger(FocusOutConversion.class);

	private static final Integer eventCode = Integer.valueOf(LibXcb.XCB_FOCUS_OUT);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	FocusOutConversion(	@Named("XEventBus") final EventBus xEventBus,
						final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		// focus in structure is the same as focus out.
		final xcb_focus_in_event_t focus_out_event = cast(event_t);

		logger.debug(	"Received X event={}",
						focus_out_event.getClass().getSimpleName());

		this.xEventBus.post(focus_out_event);

		final DisplayEvent displayEvent = new FocusLostNotify();
		return displayEvent;
	}

	private xcb_focus_in_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_focus_in_event_t(xcb_generic_event_t.getCPtr(event_t),
										false);
	}

	@Override
	public AsyncListenable getTarget(final xcb_generic_event_t event_t) {
		// focus in structure is the same as focus out.
		final xcb_focus_in_event_t focus_out_event_t = cast(event_t);
		final XWindow xWindow = this.xWindowCache.getWindow(focus_out_event_t.getEvent());

		return xWindow;
	}

	@Override
	public Integer getEventCode() {
		return eventCode;
	}
}
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
import org.trinity.foundation.display.api.event.FocusLostNotifyEvent;

import xcbjb.LibXcb;
import xcbjb.xcb_focus_in_event_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class FocusOutConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_FOCUS_OUT);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	FocusOutConversion(@Named("xEventBus") final EventBus xEventBus, final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		// focus in structure is the same as focus out.
		final xcb_focus_in_event_t focus_out_event_t = new xcb_focus_in_event_t(xcb_generic_event_t.getCPtr(event_t),
																				true);

		// TODO logging
		System.err.println(String.format(	"Received %s",
											focus_out_event_t.getClass().getSimpleName()));

		this.xEventBus.post(focus_out_event_t);

		final XWindow xWindow = this.xWindowCache.getWindow(focus_out_event_t.getEvent());
		final DisplayEvent displayEvent = new FocusLostNotifyEvent(xWindow);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}
}
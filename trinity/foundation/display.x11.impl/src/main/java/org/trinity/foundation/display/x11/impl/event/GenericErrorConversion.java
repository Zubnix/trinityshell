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

import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.display.x11.impl.XEventConversion;
import org.trinity.foundation.display.x11.impl.XcbErrorUtil;

import xcb.xcb_generic_error_t;
import xcb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
public class GenericErrorConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(0);

	private final EventBus xEventBus;

	@Inject
	GenericErrorConversion(@Named("XEventBus") final EventBus xEventBus) {
		this.xEventBus = xEventBus;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_generic_error_t request_error_t = new xcb_generic_error_t(xcb_generic_event_t
																					.getCPtr(event_t),
																			true);
		this.xEventBus.post(request_error_t);

		throw new RuntimeException(XcbErrorUtil.toString(request_error_t));
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}

}

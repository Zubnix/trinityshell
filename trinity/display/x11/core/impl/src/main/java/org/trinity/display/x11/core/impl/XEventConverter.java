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
package org.trinity.display.x11.core.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.trinity.foundation.display.api.event.DisplayEvent;

import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public final class XEventConverter {

	private final Map<Integer, XEventConversion> conversionMap = new HashMap<Integer, XEventConversion>();

	private final EventBus xEventBus;
	private final EventBus displayEventBus;

	@Inject
	XEventConverter(final Set<XEventConversion> eventConversions,
					@Named("xEventBus") final EventBus xEventBus,
					@Named("displayEventBus") final EventBus displayEventBus) {
		this.xEventBus = xEventBus;
		this.displayEventBus = displayEventBus;

		for (final XEventConversion eventConversion : eventConversions) {
			this.conversionMap.put(	eventConversion.getEventCode(),
									eventConversion);
		}

		this.xEventBus.register(this);
	}

	@Subscribe
	public void handleXEvent(final xcb_generic_event_t event_t) {
		final DisplayEvent displayEvent = convert(event_t);
		if (displayEvent != null) {
			this.displayEventBus.post(displayEvent);
		}
	}

	private DisplayEvent convert(final xcb_generic_event_t event_t) {
		final XEventConversion eventConversion = this.conversionMap.get(Integer
				.valueOf(event_t.getResponse_type()));
		if (eventConversion != null) {
			return eventConversion.convert(event_t);
		}
		return null;
	}
}
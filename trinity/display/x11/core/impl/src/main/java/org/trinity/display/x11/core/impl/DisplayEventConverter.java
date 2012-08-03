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

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind
@To(Type.IMPLEMENTATION)
public final class DisplayEventConverter {

	private final Map<Integer, EventConversion> conversionMap = new HashMap<Integer, EventConversion>();

	@Inject
	public void setEventConversions(final Set<EventConversion> eventConversions) {
		for (final EventConversion eventConversion : eventConversions) {
			this.conversionMap.put(	eventConversion.getEventCode(),
									eventConversion);
		}
	}

	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final EventConversion eventConversion = this.conversionMap.get(Integer
				.valueOf(event_t.getResponse_type()));
		if (eventConversion != null) {
			return eventConversion.convert(event_t);
		}
		return null;
	}
}
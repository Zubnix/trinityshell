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

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.ProtocolNotifyEvent;
import org.trinity.foundation.display.x11.impl.XAtomCache;
import org.trinity.foundation.display.x11.impl.XEventConversion;
import org.trinity.foundation.display.x11.impl.XWindow;
import org.trinity.foundation.display.x11.impl.XWindowCache;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class PropertyNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_PROPERTY_NOTIFY);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;
	private final XAtomCache xAtomCache;

	@Inject
	PropertyNotifyConversion(final EventBus xEventBus, final XWindowCache xWindowCache, final XAtomCache xAtomCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
		this.xAtomCache = xAtomCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_property_notify_event_t property_notify_event_t = new xcb_property_notify_event_t(xcb_generic_event_t.getCPtr(event_t),
																									true);

		// TODO logging
		System.err.println(String.format(	"Received %s",
											property_notify_event_t.getClass().getSimpleName()));

		this.xEventBus.post(property_notify_event_t);

		final int windowId = property_notify_event_t.getWindow();
		final XWindow displayEventSource = this.xWindowCache.getWindow(windowId);

		final int atomId = property_notify_event_t.getAtom();
		final String xProperty = this.xAtomCache.getAtom(atomId);

		final DisplayEvent displayEvent = new ProtocolNotifyEvent(	displayEventSource,
																	xProperty);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {

		return this.eventCode;
	}

}

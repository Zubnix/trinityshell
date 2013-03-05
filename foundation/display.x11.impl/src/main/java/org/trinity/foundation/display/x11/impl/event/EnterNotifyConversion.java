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
import org.freedesktop.xcb.xcb_enter_notify_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.PointerLeaveNotify;
import org.trinity.foundation.display.x11.impl.XEventConversion;
import org.trinity.foundation.display.x11.impl.XEventTarget;
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
public class EnterNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_ENTER_NOTIFY);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	EnterNotifyConversion(	@Named("XEventBus") final EventBus xEventBus,
							final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_enter_notify_event_t enter_notify_event_t = cast(event_t);
		// TODO logging
		System.err.println(String.format(	"Received %s",
											enter_notify_event_t.getClass().getSimpleName()));
		this.xEventBus.post(enter_notify_event_t);
		final DisplayEvent displayEvent = new PointerLeaveNotify();
		return displayEvent;
	}

	private xcb_enter_notify_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_enter_notify_event_t(xcb_generic_event_t.getCPtr(event_t),
											true);
	}

	@Override
	public XEventTarget getTarget(final xcb_generic_event_t event_t) {
		final xcb_enter_notify_event_t enter_notify_event_t = cast(event_t);
		final int windowId = enter_notify_event_t.getEvent();
		final XWindow displayEventTarget = this.xWindowCache.getWindow(windowId);
		return displayEventTarget;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}
}
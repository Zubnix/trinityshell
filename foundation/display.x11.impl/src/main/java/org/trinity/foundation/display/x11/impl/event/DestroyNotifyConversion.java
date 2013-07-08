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

import static org.freedesktop.xcb.LibXcbConstants.XCB_DESTROY_NOTIFY;

import javax.annotation.concurrent.Immutable;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_destroy_notify_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.display.x11.api.XEventConversion;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;
import org.trinity.foundation.display.x11.impl.XWindow;
import org.trinity.foundation.display.x11.impl.XWindowCache;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(multiple = true)
@Singleton
@Immutable
public class DestroyNotifyConversion implements XEventConversion {

	private static final Logger logger = LoggerFactory.getLogger(DestroyNotifyConversion.class);
	private static final Integer eventCode = XCB_DESTROY_NOTIFY;
	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	DestroyNotifyConversion(@XEventBus final EventBus xEventBus,
							final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_destroy_notify_event_t destroy_notify_event = cast(event_t);

		logger.debug(	"Received X event={}",
						destroy_notify_event.getClass().getSimpleName());

		this.xEventBus.post(destroy_notify_event);

		return new DestroyNotify();
	}

	public xcb_destroy_notify_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_destroy_notify_event_t(	xcb_generic_event_t.getCPtr(event_t),
												false);
	}

	@Override
	public AsyncListenable getTarget(final xcb_generic_event_t event_t) {
		final xcb_destroy_notify_event_t destroy_notify_event_t = cast(event_t);
		final int eventWindow = destroy_notify_event_t.getWindow();
		return this.xWindowCache.getWindow(eventWindow);
	}

	@Override
	public Integer getEventCode() {
		return eventCode;
	}
}

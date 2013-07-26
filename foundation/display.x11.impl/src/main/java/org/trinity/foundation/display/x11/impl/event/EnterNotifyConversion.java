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

import static org.freedesktop.xcb.LibXcbConstants.XCB_ENTER_NOTIFY;

import javax.annotation.concurrent.Immutable;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_enter_notify_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.PointerLeaveNotify;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XEventConversion;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;
import org.trinity.foundation.display.x11.impl.XWindowCacheImpl;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(multiple = true)
@Singleton
@ExecutionContext(DisplayExecutor.class)
@Immutable
public class EnterNotifyConversion implements XEventConversion {

	private static final Logger LOG = LoggerFactory.getLogger(EnterNotifyConversion.class);
	private static final Integer EVENT_CODE = XCB_ENTER_NOTIFY;
	private final EventBus xEventBus;
	private final XWindowCacheImpl xWindowCache;

	@Inject
	EnterNotifyConversion(	@XEventBus final EventBus xEventBus,
							final XWindowCacheImpl xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_enter_notify_event_t enter_notify_event = cast(event_t);

		LOG.debug("Received X event={}",
                enter_notify_event.getClass().getSimpleName());

		this.xEventBus.post(enter_notify_event);

		return new PointerLeaveNotify();
	}

	private xcb_enter_notify_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_enter_notify_event_t(xcb_generic_event_t.getCPtr(event_t),
											false);
	}

	@Override
	public AsyncListenable getTarget(final xcb_generic_event_t event_t) {
		final xcb_enter_notify_event_t enter_notify_event_t = cast(event_t);
		final int windowId = enter_notify_event_t.getEvent();
		return this.xWindowCache.getWindow(windowId);
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}
}

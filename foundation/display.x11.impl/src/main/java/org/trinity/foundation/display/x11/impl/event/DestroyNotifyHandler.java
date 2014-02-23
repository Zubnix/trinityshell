/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.display.x11.impl.event;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.xcb_destroy_notify_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.display.x11.api.XEventHandler;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePoolImpl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;
import javax.inject.Singleton;

import static org.freedesktop.xcb.LibXcbConstants.XCB_DESTROY_NOTIFY;

@Singleton
@Immutable
public class DestroyNotifyHandler implements XEventHandler {

	private static final Logger LOG = LoggerFactory.getLogger(DestroyNotifyHandler.class);
	private static final Integer EVENT_CODE = XCB_DESTROY_NOTIFY;
	private final EventBus xEventBus;
	private final DisplaySurfacePoolImpl xWindowPool;

	@Inject
	DestroyNotifyHandler(	@XEventBus final EventBus xEventBus,
							final DisplaySurfacePoolImpl xWindowPool) {
		this.xEventBus = xEventBus;
		this.xWindowPool = xWindowPool;
	}

	@Override
	public Optional<DestroyNotify> handle(@Nonnull final xcb_generic_event_t event_t) {
		final xcb_destroy_notify_event_t destroy_notify_event = cast(event_t);

		LOG.debug(	"Received X event={}",
					destroy_notify_event.getClass().getSimpleName());

		this.xEventBus.post(destroy_notify_event);

		return Optional.of(new DestroyNotify());
	}

	public xcb_destroy_notify_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_destroy_notify_event_t(	xcb_generic_event_t.getCPtr(event_t),
												false);
	}

	@Override
	public Optional<DisplaySurface> getTarget(@Nonnull final xcb_generic_event_t event_t) {
		final xcb_destroy_notify_event_t destroy_notify_event_t = cast(event_t);
		final int eventWindow = destroy_notify_event_t.getWindow();
		return Optional.of(this.xWindowPool.getDisplaySurface(new XWindowHandle(eventWindow)));
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}
}

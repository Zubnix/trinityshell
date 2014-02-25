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
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_unmap_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.HideNotify;
import org.trinity.foundation.display.x11.api.XEventChannel;
import org.trinity.foundation.display.x11.api.XEventHandler;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePoolImpl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_UNMAP_NOTIFY;

@Immutable
public class UnmapNotifyHandler implements XEventHandler {

	private static final Logger  LOG        = LoggerFactory.getLogger(UnmapNotifyHandler.class);
	private static final Integer EVENT_CODE = XCB_UNMAP_NOTIFY;
	private final DisplaySurfacePoolImpl xWindowPool;
	private final XEventChannel          xEventChannel;

	@Inject
	UnmapNotifyHandler(final XEventChannel xEventChannel,
					   final DisplaySurfacePoolImpl xWindowPool) {
		this.xEventChannel = xEventChannel;
		this.xWindowPool = xWindowPool;
	}

	@Override
	public Optional<HideNotify> handle(@Nonnull final xcb_generic_event_t event) {
		final xcb_unmap_notify_event_t unmap_notify_event = cast(event);

		LOG.debug("Received X event={}",
				  unmap_notify_event.getClass().getSimpleName());

		this.xEventChannel.post(unmap_notify_event);
		return Optional.of(new HideNotify());
	}

	private xcb_unmap_notify_event_t cast(final xcb_generic_event_t event) {
		return new xcb_unmap_notify_event_t(xcb_generic_event_t.getCPtr(event),
											false);
	}

	@Override
	public Optional<DisplaySurface> getTarget(@Nonnull final xcb_generic_event_t event_t) {
		final xcb_unmap_notify_event_t unmap_notify_event_t = cast(event_t);
		final int windowId = unmap_notify_event_t.getWindow();
		final int reportWindowId = unmap_notify_event_t.getEvent();
		if(windowId != reportWindowId) {
			return Optional.absent();
		}
		return Optional.of(this.xWindowPool.getDisplaySurface(XWindowHandle.create(windowId)));
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}
}

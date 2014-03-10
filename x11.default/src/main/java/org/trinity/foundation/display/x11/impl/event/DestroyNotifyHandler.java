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

import org.freedesktop.xcb.xcb_destroy_notify_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.display.x11.impl.XEventChannel;
import org.trinity.foundation.display.x11.impl.XEventHandler;
import org.trinity.foundation.display.x11.impl.XWindowPool;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_DESTROY_NOTIFY;

@Immutable
public class DestroyNotifyHandler implements XEventHandler {

	private static final Logger  LOG        = LoggerFactory.getLogger(DestroyNotifyHandler.class);
	private static final Integer EVENT_CODE = XCB_DESTROY_NOTIFY;
	private final XEventChannel xEventChannel;
	private final XWindowPool   xWindowPool;

	@Inject
	DestroyNotifyHandler(final XEventChannel xEventChannel,
						 final XWindowPool xWindowPool) {
		this.xEventChannel = xEventChannel;
		this.xWindowPool = xWindowPool;
	}

	@Override
	public void handle(@Nonnull final xcb_generic_event_t event_t) {
		final xcb_destroy_notify_event_t destroy_notify_event = cast(event_t);

		LOG.debug("Received X event={}",
				  destroy_notify_event.getClass().getSimpleName());

		this.xEventChannel.post(destroy_notify_event);
		final int windowId = destroy_notify_event.getWindow();
		this.xWindowPool.get(windowId).post(destroy_notify_event);
	}

	public xcb_destroy_notify_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_destroy_notify_event_t(xcb_generic_event_t.getCPtr(event_t),
											  false);
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}
}
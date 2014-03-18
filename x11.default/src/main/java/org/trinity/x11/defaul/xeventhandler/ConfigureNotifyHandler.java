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
package org.trinity.x11.defaul.xeventhandler;

import org.freedesktop.xcb.xcb_configure_notify_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.x11.defaul.XEventChannel;
import org.trinity.x11.defaul.XEventHandler;
import org.trinity.x11.defaul.XSurfacePool;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_CONFIGURE_NOTIFY;

@Immutable
public class ConfigureNotifyHandler implements XEventHandler {

	private static final Logger  LOG        = LoggerFactory.getLogger(ConfigureNotifyHandler.class);
	private static final Integer EVENT_CODE = XCB_CONFIGURE_NOTIFY;

	private final XSurfacePool  xSurfacePool;
	private final XEventChannel xEventChannel;

	@Inject
	ConfigureNotifyHandler(final XSurfacePool xSurfacePool,
						   final XEventChannel xEventChannel) {
		this.xEventChannel = xEventChannel;
		this.xSurfacePool = xSurfacePool;
	}

	@Override
	public void handle(@Nonnull final xcb_generic_event_t event_t) {
		final xcb_configure_notify_event_t configure_notify_event = cast(event_t);

		LOG.debug("Received X event={}",
				  configure_notify_event.getClass().getSimpleName());

		this.xEventChannel.post(configure_notify_event);
		final int windowId = configure_notify_event.getWindow();
		this.xSurfacePool.get(windowId).post(configure_notify_event);
	}

	private xcb_configure_notify_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_configure_notify_event_t(xcb_generic_event_t.getCPtr(event_t),
												false);
	}

	@Override
	public Integer getEventCode() {
		return EVENT_CODE;
	}

}

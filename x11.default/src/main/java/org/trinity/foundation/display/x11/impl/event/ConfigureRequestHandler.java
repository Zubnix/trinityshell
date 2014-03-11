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

import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.display.x11.impl.XEventChannel;
import org.trinity.foundation.display.x11.impl.XEventHandler;
import org.trinity.foundation.display.x11.impl.XSurfacePool;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_CONFIGURE_REQUEST;

@Immutable
public class ConfigureRequestHandler implements XEventHandler {

	private static final Logger  LOG        = LoggerFactory.getLogger(ConfigureRequestHandler.class);
	private static final Integer EVENT_CODE = XCB_CONFIGURE_REQUEST;

	private final XEventChannel xEventChannel;
	private final XSurfacePool xSurfacePool;

	@Inject
	ConfigureRequestHandler(final XEventChannel xEventChannel,
							final XSurfacePool xSurfacePool) {
		this.xEventChannel = xEventChannel;
		this.xSurfacePool = xSurfacePool;
	}

	@Override
	public void handle(@Nonnull final xcb_generic_event_t event_t) {
		final xcb_configure_request_event_t request_event = cast(event_t);

		LOG.debug("Received X event={}",
				  request_event.getClass().getSimpleName());

		this.xEventChannel.post(request_event);
		final int windowId = request_event.getWindow();
		this.xSurfacePool.get(windowId).post(request_event);

//		final int x = request_event.getX();
//		final int y = request_event.getY();
//		final int width = request_event.getWidth() + (2 * request_event.getBorder_width());
//		final int height = request_event.getHeight() + (2 * request_event.getBorder_width());
//		final Rectangle geometry = new Rectangle(x,
//												 y,
//												 width,
//												 height);
//
//		final int valueMask = request_event.getValue_mask();
//
//		final boolean configureX = (valueMask & XCB_CONFIG_WINDOW_X) != 0;
//		final boolean configureY = (valueMask & XCB_CONFIG_WINDOW_Y) != 0;
//		final boolean configureWidth = (valueMask & XCB_CONFIG_WINDOW_WIDTH) != 0;
////		final boolean configureHeight = (valueMask & XCB_CONFIG_WINDOW_HEIGHT) != 0;
	}

	private xcb_configure_request_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_configure_request_event_t(xcb_generic_event_t.getCPtr(event_t),
												 false);
	}

	@Override
	public Integer getEventCode() {

		return EVENT_CODE;
	}
}

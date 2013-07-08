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

import static org.freedesktop.xcb.LibXcbConstants.XCB_CONFIGURE_REQUEST;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_X;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_Y;

import javax.annotation.concurrent.Immutable;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplayServer;
import org.trinity.foundation.api.display.event.CreationNotify;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.GeometryRequest;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ImmutableRectangle;
import org.trinity.foundation.api.shared.Rectangle;
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
public class ConfigureRequestConversion implements XEventConversion {

	private static final Logger logger = LoggerFactory.getLogger(ConfigureRequestConversion.class);
	private final Integer eventCode = XCB_CONFIGURE_REQUEST;
	private final XWindowCache xWindowCache;
	private final EventBus xEventBus;
	private final DisplayServer displayServer;

	@Inject
	ConfigureRequestConversion(	@XEventBus final EventBus xEventBus,
								final XWindowCache xWindowCache,
								final DisplayServer displayServer) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
		this.displayServer = displayServer;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_configure_request_event_t request_event = cast(event_t);

		logger.debug(	"Received X event={}",
						request_event.getClass().getSimpleName());

		this.xEventBus.post(request_event);

		final int x = request_event.getX();
		final int y = request_event.getY();
		final int width = request_event.getWidth() + (2 * request_event.getBorder_width());
		final int height = request_event.getHeight() + (2 * request_event.getBorder_width());
		final Rectangle geometry = new ImmutableRectangle(	x,
															y,
															width,
															height);

		final int valueMask = request_event.getValue_mask();

		final boolean configureX = (valueMask & XCB_CONFIG_WINDOW_X) != 0;
		final boolean configureY = (valueMask & XCB_CONFIG_WINDOW_Y) != 0;
		final boolean configureWidth = (valueMask & XCB_CONFIG_WINDOW_WIDTH) != 0;
		final boolean configureHeight = (valueMask & XCB_CONFIG_WINDOW_HEIGHT) != 0;

		return new GeometryRequest(	geometry,
									configureX,
									configureY,
									configureWidth,
									configureHeight);
	}

	private xcb_configure_request_event_t cast(final xcb_generic_event_t event_t) {
		return new xcb_configure_request_event_t(	xcb_generic_event_t.getCPtr(event_t),
													false);
	}

	@Override
	public AsyncListenable getTarget(final xcb_generic_event_t event_t) {
		final xcb_configure_request_event_t request_event_t = cast(event_t);
		final int windowId = request_event_t.getWindow();

		final boolean present = this.xWindowCache.isPresent(windowId);
		final XWindow displayEventTarget = this.xWindowCache.getWindow(windowId);
		if (!present) {
			displayEventTarget.configureClientEvents();
			// this is a bit of a dirty hack to work around X's model of client
			// discovery.
			final CreationNotify creationNotify = new CreationNotify(displayEventTarget);
			this.displayServer.post(creationNotify);
		}

		return displayEventTarget;
	}

	@Override
	public Integer getEventCode() {

		return this.eventCode;
	}
}

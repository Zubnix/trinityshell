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
package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.GeometryRequestEvent;
import org.trinity.foundation.shared.geometry.api.ImmutableRectangle;
import org.trinity.foundation.shared.geometry.api.Rectangle;

import xcbjb.LibXcb;
import xcbjb.xcb_config_window_t;
import xcbjb.xcb_configure_request_event_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class ConfigureRequestConversion implements XEventConversion {

	private final Integer eventInteger = Integer.valueOf(LibXcb.XCB_CONFIGURE_REQUEST);

	private final XWindowCache xWindowCache;
	private final EventBus xEventBus;

	@Inject
	ConfigureRequestConversion(@Named("xEventBus") final EventBus xEventBus, final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_configure_request_event_t request_event_t = new xcb_configure_request_event_t(xcb_generic_event_t.getCPtr(event_t),
																								true);

		// TODO logging
		System.err.println(String.format(	"Received %s",
											request_event_t.getClass().getSimpleName()));

		this.xEventBus.post(request_event_t);

		final int windowId = request_event_t.getWindow();
		final XWindow displayEventSource = this.xWindowCache.getWindow(windowId);

		final int x = request_event_t.getX();
		final int y = request_event_t.getY();
		final int width = request_event_t.getWidth();
		final int height = request_event_t.getHeight();
		final Rectangle geometry = new ImmutableRectangle(	x,
															y,
															width,
															height);

		final int valueMask = request_event_t.getValue_mask();
		final boolean configureX = (valueMask | xcb_config_window_t.XCB_CONFIG_WINDOW_X) != 0;
		final boolean configureY = (valueMask | xcb_config_window_t.XCB_CONFIG_WINDOW_Y) != 0;
		final boolean configureWidth = (valueMask | xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH) != 0;
		final boolean configureHeight = (valueMask | xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT) != 0;

		displayEventSource.configureClientEvents();

		final DisplayEvent displayEvent = new GeometryRequestEvent(	displayEventSource,
																	geometry,
																	configureX,
																	configureY,
																	configureWidth,
																	configureHeight);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {

		return this.eventInteger;
	}
}
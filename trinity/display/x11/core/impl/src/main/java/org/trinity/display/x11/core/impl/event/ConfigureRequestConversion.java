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

	private final Integer eventInteger = Integer
			.valueOf(LibXcb.XCB_CONFIGURE_REQUEST);

	private final XWindowCache xWindowCache;
	private final EventBus xEventBus;

	@Inject
	ConfigureRequestConversion(	@Named("xEventBus") final EventBus xEventBus,
								final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_configure_request_event_t request_event_t = new xcb_configure_request_event_t(xcb_generic_event_t
																										.getCPtr(event_t),
																								true);

		this.xEventBus.post(request_event_t);

		final int windowId = request_event_t.getWindow();
		final XWindow displayEventSource = this.xWindowCache
				.getWindow(windowId);

		final int x = request_event_t.getX();
		final int y = request_event_t.getY();
		final int width = request_event_t.getWidth();
		final int height = request_event_t.getHeight();
		final Rectangle geometry = new ImmutableRectangle(x, y, width, height);

		final int valueMask = request_event_t.getValue_mask();
		final boolean configureX = (valueMask | xcb_config_window_t.XCB_CONFIG_WINDOW_X
				.swigValue()) != 0;
		final boolean configureY = (valueMask | xcb_config_window_t.XCB_CONFIG_WINDOW_Y
				.swigValue()) != 0;
		final boolean configureWidth = (valueMask | xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH
				.swigValue()) != 0;
		final boolean configureHeight = (valueMask | xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT
				.swigValue()) != 0;

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
package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.GeometryNotifyEvent;
import org.trinity.foundation.shared.geometry.api.ImmutableRectangle;
import org.trinity.foundation.shared.geometry.api.Rectangle;

import xcbjb.LibXcb;
import xcbjb.xcb_configure_notify_event_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class ConfigureNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_CONFIGURE_NOTIFY);

	private final XWindowCache xWindowCache;
	private final EventBus xEventBus;

	@Inject
	ConfigureNotifyConversion(final XWindowCache xWindowCache, @Named("xEventBus") final EventBus xEventBus) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_configure_notify_event_t configure_notify_event_t = new xcb_configure_notify_event_t(	xcb_generic_event_t
																												.getCPtr(event_t),
																										true);
		this.xEventBus.post(configure_notify_event_t);

		final int windowId = (int) configure_notify_event_t.getEvent();
		final XWindow displayEventSource = this.xWindowCache.getWindow(windowId);

		final int x = configure_notify_event_t.getX();
		final int y = configure_notify_event_t.getY();
		final int width = configure_notify_event_t.getWidth();
		final int height = configure_notify_event_t.getHeight();
		final Rectangle geometry = new ImmutableRectangle(	x,
															y,
															width,
															height);

		final DisplayEvent displayEvent = new GeometryNotifyEvent(	displayEventSource,
																	geometry);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}

}

package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.HideNotifyEvent;

import xcbjb.LibXcb;
import xcbjb.xcb_generic_event_t;
import xcbjb.xcb_map_notify_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class MapNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_MAP_NOTIFY);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	MapNotifyConversion(@Named("xEventBus") final EventBus xEventBus, final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_map_notify_event_t map_notify_event_t = new xcb_map_notify_event_t(	xcb_generic_event_t.getCPtr(event_t),
																						true);
		this.xEventBus.post(map_notify_event_t);

		final int windowId = map_notify_event_t.getWindow();
		final XWindow displayEventSource = this.xWindowCache.getWindow(windowId);

		final DisplayEvent displayEvent = new HideNotifyEvent(displayEventSource);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}
}
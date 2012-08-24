package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DestroyNotifyEvent;
import org.trinity.foundation.display.api.event.DisplayEvent;

import xcbjb.LibXcb;
import xcbjb.xcb_destroy_notify_event_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class DestroyNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_DESTROY_NOTIFY);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	DestroyNotifyConversion(@Named("xEventBus") final EventBus xEventBus, final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_destroy_notify_event_t destroy_notify_event_t = new xcb_destroy_notify_event_t(	xcb_generic_event_t.getCPtr(event_t),
																									true);
		this.xEventBus.post(destroy_notify_event_t);

		final int eventWindow = destroy_notify_event_t.getEvent();
		final XWindow displayEventSource = this.xWindowCache.getWindow(eventWindow);

		final DisplayEvent displayEvent = new DestroyNotifyEvent(displayEventSource);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}
}
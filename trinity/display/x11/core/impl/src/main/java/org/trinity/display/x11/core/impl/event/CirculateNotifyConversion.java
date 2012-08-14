package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.StackingChangedNotifyEvent;

import xcbjb.LibXcb;
import xcbjb.xcb_circulate_notify_event_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class CirculateNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer
			.valueOf(LibXcb.XCB_CIRCULATE_NOTIFY);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	CirculateNotifyConversion(	@Named("xEventBus") final EventBus xEventBus,
								final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_circulate_notify_event_t circulate_notify_event_t = new xcb_circulate_notify_event_t(	xcb_generic_event_t
																												.getCPtr(event_t),
																										true);
		this.xEventBus.post(circulate_notify_event_t);

		final int windowId = circulate_notify_event_t.getEvent();
		final XWindow displayEventSource = this.xWindowCache
				.getWindow(windowId);

		final DisplayEvent displayEvent = new StackingChangedNotifyEvent(displayEventSource);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}
}
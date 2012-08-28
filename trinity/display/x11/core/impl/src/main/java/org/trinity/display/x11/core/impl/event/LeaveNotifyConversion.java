package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.PointerLeaveNotifyEvent;

import xcbjb.LibXcb;
import xcbjb.xcb_enter_notify_event_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class LeaveNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_LEAVE_NOTIFY);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	LeaveNotifyConversion(@Named("xEventBus") final EventBus xEventBus, final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		// enter has same structure as leave
		final xcb_enter_notify_event_t enter_notify_event_t = new xcb_enter_notify_event_t(	xcb_generic_event_t.getCPtr(event_t),
																							true);
		this.xEventBus.post(enter_notify_event_t);

		final int windowId = (int) enter_notify_event_t.getEvent();
		final XWindow displayEventSource = this.xWindowCache.getWindow(windowId);

		final DisplayEvent displayEvent = new PointerLeaveNotifyEvent(displayEventSource);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}
}
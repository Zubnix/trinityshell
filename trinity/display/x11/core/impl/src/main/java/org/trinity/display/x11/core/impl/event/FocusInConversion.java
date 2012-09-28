package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.FocusGainNotifyEvent;

import xcbjb.LibXcb;
import xcbjb.xcb_focus_in_event_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class FocusInConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_FOCUS_IN);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	FocusInConversion(final EventBus xEventBus, final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_focus_in_event_t focus_in_event_t = new xcb_focus_in_event_t(	xcb_generic_event_t.getCPtr(event_t),
																				true);

		// TODO logging
		System.err.println(String.format(	"Received %s",
											focus_in_event_t.getClass().getSimpleName()));

		this.xEventBus.post(focus_in_event_t);

		final int windowId = focus_in_event_t.getEvent();
		final XWindow displayEventSource = this.xWindowCache.getWindow(windowId);
		final DisplayEvent displayEvent = new FocusGainNotifyEvent(displayEventSource);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}
}
package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.ProtocolNotifyEvent;

import xcbjb.LibXcb;
import xcbjb.xcb_generic_event_t;
import xcbjb.xcb_property_notify_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class PropertyNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer
			.valueOf(LibXcb.XCB_PROPERTY_NOTIFY);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;

	@Inject
	PropertyNotifyConversion(	final EventBus xEventBus,
								final XWindowCache xWindowCache) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_property_notify_event_t property_notify_event_t = new xcb_property_notify_event_t(xcb_generic_event_t
																											.getCPtr(event_t),
																									true);
		this.xEventBus.post(property_notify_event_t);

		final int windowId = property_notify_event_t.getWindow();
		final XWindow displayEventSource = this.xWindowCache
				.getWindow(windowId);

		final DisplayEvent displayEvent = new ProtocolNotifyEvent(	displayEventSource,
																	displayProtocol);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {

		return this.eventCode;
	}

}

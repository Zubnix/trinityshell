package org.trinity.display.x11.core.impl.event;

import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.foundation.display.api.event.DisplayEvent;

import xcbjb.xcb_generic_error_t;
import xcbjb.xcb_generic_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
public class GenericErrorConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(0);

	private final EventBus xEventBus;

	@Inject
	GenericErrorConversion(@Named("xEventBus") final EventBus xEventBus) {
		this.xEventBus = xEventBus;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {
		final xcb_generic_error_t request_error_t = new xcb_generic_error_t(xcb_generic_event_t.getCPtr(event_t),
																			true);
		this.xEventBus.post(request_error_t);

		throw new Error("xcb error: " + request_error_t.getError_code());
	}

	@Override
	public Integer getEventCode() {
		return this.eventCode;
	}

}

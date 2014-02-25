package org.trinity.foundation.display.x11.impl;

import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.display.x11.api.XEventChannel;

import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_wait_for_event;

public class XEventPump {

	private static final Logger LOG = LoggerFactory.getLogger(XEventPump.class);

	private final XEventChannel xEventChannel;

	@Inject
	XEventPump(final XEventChannel xEventChannel) {
		this.xEventChannel = xEventChannel;

	}

	public void pump() {

		if(xcb_connection_has_error(this.xEventChannel.getConnectionReference()) != 0) {
			final String errorMsg = "X11 connection was closed unexpectedly - maybe your X server terminated / crashed?";
			LOG.error(errorMsg);
			throw new Error(errorMsg);
		}

		final xcb_generic_event_t xcb_generic_event = xcb_wait_for_event(this.xEventChannel.getConnectionReference());

		try {
			//acquire permit
			this.xEventChannel.stop();
			this.xEventChannel.post(xcb_generic_event);
			xcb_generic_event.delete();

			// schedule next event retrieval
			//  this.xEventPumpExecutor.submit(this);
		}
		finally {
			//release permit
			this.xEventChannel.start();
		}
	}
}

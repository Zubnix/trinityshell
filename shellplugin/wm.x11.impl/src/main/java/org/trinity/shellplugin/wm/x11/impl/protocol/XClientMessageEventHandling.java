package org.trinity.shellplugin.wm.x11.impl.protocol;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.xcb_client_message_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.shellplugin.wm.x11.impl.XEventHandling;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class XClientMessageEventHandling implements XEventHandling {

	private static final Integer eventCode = Integer.valueOf(LibXcb.XCB_CLIENT_MESSAGE);

	private final XWindowProcol xWindowProcol;

	@Inject
	XClientMessageEventHandling(final XWindowProcol xWindowProcol) {
		this.xWindowProcol = xWindowProcol;
	}

	@Override
	public void handle(final xcb_generic_event_t xEvent) {
		final xcb_client_message_event_t client_message_event_t = new xcb_client_message_event_t(	xcb_generic_event_t.getCPtr(xEvent),
																									true);
		this.xWindowProcol.findXWindow(client_message_event_t.getWindow()).get().post(client_message_event_t);
	}

	@Override
	public Integer getEventCode() {
		return eventCode;
	}
}
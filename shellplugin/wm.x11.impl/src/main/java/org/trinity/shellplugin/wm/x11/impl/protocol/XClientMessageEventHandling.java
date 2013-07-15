package org.trinity.shellplugin.wm.x11.impl.protocol;

import static org.freedesktop.xcb.LibXcbConstants.XCB_CLIENT_MESSAGE;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_client_message_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(multiple = true)
@Singleton
public class XClientMessageEventHandling implements XEventHandling {

	private static final Integer eventCode = XCB_CLIENT_MESSAGE;
	private final XWindowProtocol xWindowProtocol;

	@Inject
	XClientMessageEventHandling(final XWindowProtocol xWindowProtocol) {
		this.xWindowProtocol = xWindowProtocol;
	}

	@Override
	public void handle(final xcb_generic_event_t xEvent) {
		final xcb_client_message_event_t client_message_event_t = new xcb_client_message_event_t(	xcb_generic_event_t.getCPtr(xEvent),
																									false);
		this.xWindowProtocol.findXWindow(client_message_event_t.getWindow()).get().post(client_message_event_t);
	}

	@Override
	public Integer getEventCode() {
		return eventCode;
	}
}

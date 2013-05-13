package org.trinity.shellplugin.wm.x11.impl.protocol;

import org.freedesktop.xcb.xcb_client_message_data_t;
import org.freedesktop.xcb.xcb_client_message_event_t;
import org.freedesktop.xcb.xcb_event_mask_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shellplugin.wm.x11.impl.XConnection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_send_event;
import static org.freedesktop.xcb.LibXcbConstants.XCB_CLIENT_MESSAGE;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
public class XClientMessageSender {
	private final XConnection xConnection;

	@Inject
	XClientMessageSender(final XConnection xConnection) {
		this.xConnection = xConnection;
	}

	public void sendMessage(final DisplaySurface xWindow,
							final int type,
							final int format,
							final xcb_client_message_data_t message) {

		final Integer winId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();

		final xcb_client_message_event_t client_message_event = new xcb_client_message_event_t();
		client_message_event.setFormat((short) format);
		client_message_event.setData(message);
		client_message_event.setType(type);
		client_message_event.setWindow(winId.intValue());
		client_message_event.setResponse_type((short) XCB_CLIENT_MESSAGE);

		final xcb_generic_event_t generic_event = new xcb_generic_event_t(	xcb_client_message_event_t.getCPtr(client_message_event),
																			true);
		xcb_send_event(	this.xConnection.getConnectionRef(),
						(short) 0,
						winId.intValue(),
						xcb_event_mask_t.XCB_EVENT_MASK_NO_EVENT,
						generic_event);
		xcb_flush(this.xConnection.getConnectionRef());
	}
}

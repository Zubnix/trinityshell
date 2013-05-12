package org.trinity.shellplugin.wm.x11.impl.protocol;

import static org.freedesktop.xcb.LibXcb.xcb_send_event;
import static org.freedesktop.xcb.LibXcbConstants.XCB_CLIENT_MESSAGE;

import org.freedesktop.xcb.xcb_client_message_data_t;
import org.freedesktop.xcb.xcb_client_message_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shellplugin.wm.x11.impl.XConnection;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
public class XClientMessageSender {
	private final XConnection xConnection;

	public enum Format {
		Byte((short) 8), Short((short) 16), Integer((short) 32);

		private final short format;

		private Format(final short format) {
			this.format = format;

		}

		public short getFormat() {
			return this.format;
		}
	}

	@Inject
	XClientMessageSender(final XConnection xConnection) {
		this.xConnection = xConnection;
	}

	public void sendMessage(final DisplaySurface xWindow,
							final int type,
							final Format format,
							final xcb_client_message_data_t message) {

		final Integer winId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		final short messageFormat = format.getFormat();

		final xcb_client_message_event_t client_message_event = new xcb_client_message_event_t();
		client_message_event.setFormat(messageFormat);
		client_message_event.setData(message);
		client_message_event.setType(type);
		client_message_event.setWindow(winId.intValue());
		client_message_event.setType(XCB_CLIENT_MESSAGE);

		final xcb_generic_event_t generic_event = new xcb_generic_event_t(	xcb_client_message_event_t.getCPtr(client_message_event),
																			true);
		xcb_send_event(	this.xConnection.getConnectionRef(),
						(short) 0,
						winId.intValue(),
						0,
						generic_event);
	}
}

package org.trinity.display.x11.core.impl.property;

import java.util.List;
import java.util.Map;

import org.trinity.display.x11.core.impl.XAtomCache;
import org.trinity.display.x11.core.impl.XConnection;
import org.trinity.display.x11.core.impl.XDisplayProtocolHandler;
import org.trinity.display.x11.core.impl.XPropertyCache;
import org.trinity.display.x11.core.impl.XWindowHandle;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.foundation.display.api.DisplayProtocol;

import xcbjb.LibXcb;
import xcbjb.LibXcbConstants;
import xcbjb.xcb_client_message_data_t;
import xcbjb.xcb_client_message_event_t;
import xcbjb.xcb_event_mask_t;
import xcbjb.xcb_generic_event_t;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class CloseRequestProtocolHandler implements XDisplayProtocolHandler {

	private final XPropertyCache xPropertyCache;
	private final XAtomCache xAtomCache;
	private final XConnection xConnection;

	@Inject
	CloseRequestProtocolHandler(final XConnection xConnection,
								final XAtomCache xAtomCache,
								final XPropertyCache xPropertyCache) {
		this.xPropertyCache = xPropertyCache;
		this.xAtomCache = xAtomCache;
		this.xConnection = xConnection;
	}

	@Override
	public Map<String, Object> handleDipslayProtocol(	final XWindow xWindow,
														final DisplayProtocol displayProtocol,
														final Map<String, Object> arguments) {

		final Map<String, Object> wmProtocolsValue = this.xPropertyCache.getXProperty(	xWindow,
																						"WM_PROTOCOLS");
		@SuppressWarnings("unchecked")
		final List<String> protocols = (List<String>) wmProtocolsValue.get("ATOM");

		final int windowId = ((XWindowHandle) xWindow.getDisplaySurfaceHandle()).getNativeHandle();
		if (protocols.contains("WM_DELETE_WINDOW")) {
			final xcb_client_message_event_t client_message_event_t = new xcb_client_message_event_t();
			client_message_event_t.setType(LibXcbConstants.XCB_CLIENT_MESSAGE);
			client_message_event_t.setWindow(windowId);
			final int wmProtocolsId = this.xAtomCache.getAtom("WM_PROTOCOLS");
			client_message_event_t.setType(wmProtocolsId);
			client_message_event_t.setFormat((short) 32);

			final xcb_client_message_data_t client_message_data_t = new xcb_client_message_data_t();
			final int wmDeleteWindowId = this.xAtomCache.getAtom("WM_DELETE_WINDOW");
			client_message_data_t.setData32(new int[] { wmDeleteWindowId, 0 });
			client_message_event_t.setData(client_message_data_t);

			LibXcb.xcb_send_event(	this.xConnection.getConnectionReference(),
									(short) 0,
									windowId,
									xcb_event_mask_t.XCB_EVENT_MASK_NO_EVENT.swigValue(),
									new xcb_generic_event_t(xcb_client_message_event_t.getCPtr(client_message_event_t),
															true));
		} else {

			LibXcb.xcb_kill_client(	this.xConnection.getConnectionReference(),
									windowId);
		}

		return null;
	}

	@Override
	public DisplayProtocol getDisplayProtocol() {
		return DisplayProtocol.CLOSE_REQUEST;
	}

}

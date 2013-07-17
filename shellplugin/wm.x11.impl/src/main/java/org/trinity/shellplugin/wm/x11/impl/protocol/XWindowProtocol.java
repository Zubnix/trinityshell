package org.trinity.shellplugin.wm.x11.impl.protocol;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_cw_t;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;

@NotThreadSafe
@Bind
@To(IMPLEMENTATION)
@Singleton
@ExecutionContext(DisplayExecutor.class)
public class XWindowProtocol {

	private static final ByteBuffer PROPERTY_MASK = allocateDirect(4).order(nativeOrder())
			.putInt(XCB_EVENT_MASK_PROPERTY_CHANGE);
	private final Map<Integer, DisplaySurface> clientsById = new HashMap<Integer, DisplaySurface>();
	private final XConnection xConnection;

	@Inject
	XWindowProtocol(final XConnection xConnection) {
		this.xConnection = xConnection;
	}

	public DisplaySurface findXWindow(final int clientId) {
		return this.clientsById.get(Integer.valueOf(clientId));
	}

	public void register(final DisplaySurface xWindow) {
		final Integer xWindowId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		this.clientsById.put(	xWindowId,
								xWindow);
		listenForXProtocol(xWindowId);
	}

	private void listenForXProtocol(final Integer xWindowId) {
		xcb_change_window_attributes(	this.xConnection.getConnectionReference(),
										xWindowId.intValue(),
										xcb_cw_t.XCB_CW_EVENT_MASK,
										PROPERTY_MASK);
		xcb_flush(this.xConnection.getConnectionReference());
	}
}

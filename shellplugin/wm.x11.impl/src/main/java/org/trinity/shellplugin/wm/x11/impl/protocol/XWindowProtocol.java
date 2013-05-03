package org.trinity.shellplugin.wm.x11.impl.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.xcb_cw_t;
import org.freedesktop.xcb.xcb_event_mask_t;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shellplugin.wm.x11.impl.XConnection;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_flush;

@NotThreadSafe
@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
@OwnerThread("WindowManager")
public class XWindowProtocol {

	private final Map<Integer, Optional<DisplaySurface>> clientsById = new HashMap<Integer, Optional<DisplaySurface>>();

	private final XConnection xConnection;

	@Inject
	XWindowProtocol(final XConnection xConnection) {
		this.xConnection = xConnection;
	}

	public Optional<DisplaySurface> findXWindow(final int clientId) {
		final Optional<DisplaySurface> optionalDisplaySurface = this.clientsById.get(Integer.valueOf(clientId));
		if (!optionalDisplaySurface.isPresent()) {
			return Optional.absent();
		}
		return optionalDisplaySurface;
	}

	public void register(final DisplaySurface xWindow) {
		final Integer xWindowId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		this.clientsById.put(	xWindowId,
								Optional.of(xWindow));
		listenForXProtocol(xWindowId);
	}

	private void listenForXProtocol(final Integer xWindowId) {
		final ByteBuffer values = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE);

		xcb_change_window_attributes(	this.xConnection.getConnectionRef(),
										xWindowId.intValue(),
										xcb_cw_t.XCB_CW_EVENT_MASK,
										values);
		xcb_flush(this.xConnection.getConnectionRef());
	}
}
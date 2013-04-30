package org.trinity.shellplugin.wm.x11.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;
import static org.freedesktop.xcb.LibXcb.xcb_connect;

@Singleton
@Bind(to = @To(value = Type.IMPLEMENTATION))
public class XConnection {

	private final SWIGTYPE_p_xcb_connection_t connectionRef;

	public XConnection() {
		final String displayname = System.getenv("DISPLAY");
		final ByteBuffer screenp = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		screenp.putInt(0);
		this.connectionRef = xcb_connect(	displayname,
											screenp);
	}

	public SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		return this.connectionRef;
	}
}
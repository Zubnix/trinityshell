/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.x11.impl;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_connect;
import static org.freedesktop.xcb.LibXcb.xcb_disconnect;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_cw_t;
import org.freedesktop.xcb.xcb_event_mask_t;
import org.freedesktop.xcb.xcb_screen_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;

import com.google.inject.Singleton;

@Bind
@To(IMPLEMENTATION)
@Singleton
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public class XConnectionImpl implements XConnection {

	private SWIGTYPE_p_xcb_connection_t xcb_connection;

	XConnectionImpl() {
	}

	@Override
	public void open(@Nonnull final String displayName,
					@Nonnegative final int screen) {
		final ByteBuffer screenBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		screenBuf.putInt(screen);
		this.xcb_connection = xcb_connect(displayName,
										screenBuf);
	}

	@Override
	public void close() {
		xcb_disconnect(this.xcb_connection);
	}

	@Override
	public SWIGTYPE_p_xcb_connection_t getConnectionReference() {
		return this.xcb_connection;
	}
}

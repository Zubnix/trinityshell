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
package org.trinity.display.x11.core.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import xcbjb.LibXcb;
import xcbjb.SWIGTYPE_p_xcb_connection_t;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class XConnection {

	private SWIGTYPE_p_xcb_connection_t connection_t;

	public void open(final String displayName, final int screen) {
		final ByteBuffer screenBuf = ByteBuffer.allocateDirect(4)
				.order(ByteOrder.nativeOrder());
		screenBuf.putInt(screen);
		this.connection_t = LibXcb.xcb_connect(displayName, screenBuf);
	}

	public void close() {
		LibXcb.xcb_disconnect(this.connection_t);
	}

	public SWIGTYPE_p_xcb_connection_t getConnectionReference() {
		return this.connection_t;
	}
}

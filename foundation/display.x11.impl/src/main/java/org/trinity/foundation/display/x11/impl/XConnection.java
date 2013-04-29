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

import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_connect;
import static org.freedesktop.xcb.LibXcb.xcb_disconnect;
import static org.freedesktop.xcb.LibXcb.xcb_get_setup;
import static org.freedesktop.xcb.LibXcb.xcb_screen_next;
import static org.freedesktop.xcb.LibXcb.xcb_setup_roots_iterator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.concurrent.NotThreadSafe;

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_cw_t;
import org.freedesktop.xcb.xcb_event_mask_t;
import org.freedesktop.xcb.xcb_screen_iterator_t;
import org.freedesktop.xcb.xcb_screen_t;

import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
@NotThreadSafe
public class XConnection {

	private SWIGTYPE_p_xcb_connection_t connection_t;
	private xcb_screen_t screen_t;

	public void open(	final String displayName,
						final int screen) {
		final ByteBuffer screenBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		screenBuf.putInt(screen);
		this.connection_t = xcb_connect(displayName,
										screenBuf);

		int targetScreen = screen;
		final xcb_screen_iterator_t iter = xcb_setup_roots_iterator(xcb_get_setup(this.connection_t));
		for (; iter.getRem() != 0; --targetScreen, xcb_screen_next(iter)) {
			if (targetScreen == 0) {
				this.screen_t = iter.getData();
				break;
			}
		}

		configureRootEvents();
	}

	private void configureRootEvents() {
		final int rootId = this.screen_t.getRoot();

		final ByteBuffer values = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE
				| xcb_event_mask_t.XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT);

		xcb_change_window_attributes(	getConnectionReference(),
										rootId,
										xcb_cw_t.XCB_CW_EVENT_MASK,
										values);
	}

	public void close() {

		xcb_disconnect(this.connection_t);
	}

	public SWIGTYPE_p_xcb_connection_t getConnectionReference() {
		return this.connection_t;
	}

	public xcb_screen_t getScreenReference() {
		return this.screen_t;
	}
}

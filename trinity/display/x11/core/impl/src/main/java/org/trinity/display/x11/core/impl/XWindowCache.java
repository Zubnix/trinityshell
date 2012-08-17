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
import java.util.HashMap;
import java.util.Map;

import org.trinity.foundation.display.api.DisplayRenderAreaFactory;
import org.trinity.foundation.display.api.DisplayResourceHandle;
import org.trinity.foundation.display.api.DisplayResourceHandleFactory;

import xcbjb.LibXcb;
import xcbjb.xcb_cw_t;
import xcbjb.xcb_event_mask_t;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
@Singleton
public class XWindowCache {

	public final Map<Integer, XWindow> windows = new HashMap<Integer, XWindow>();

	private final DisplayResourceHandleFactory resourceHandleFactory;
	private final DisplayRenderAreaFactory displayResourceFactory;
	private final XConnection xConnection;

	@Inject
	XWindowCache(	final XConnection xConnection,
					final DisplayResourceHandleFactory resourceHandleFactory,
					final DisplayRenderAreaFactory displayResourceFactory) {
		this.xConnection = xConnection;
		this.resourceHandleFactory = resourceHandleFactory;
		this.displayResourceFactory = displayResourceFactory;
	}

	public XWindow getWindow(final int windowId) {
		synchronized (this.windows) {
			XWindow window = this.windows.get(Integer.valueOf(windowId));
			if (window == null) {
				final Integer windowID = Integer.valueOf(windowId);
				final DisplayResourceHandle resourceHandle = this.resourceHandleFactory
						.createResourceHandle(windowID);
				window = (XWindow) this.displayResourceFactory
						.createDisplayRenderArea(resourceHandle);
				this.windows.put(windowID, window);
				configureClientEvents(windowID);
			}
			return window;
		}
	}

	private void configureClientEvents(final Integer windowId) {
		final ByteBuffer values = ByteBuffer.allocateDirect(4 + 4 + 4 + 4)
				.order(ByteOrder.nativeOrder());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE
				.swigValue());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_ENTER_WINDOW.swigValue());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_LEAVE_WINDOW.swigValue());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_STRUCTURE_NOTIFY
				.swigValue());

		LibXcb.xcb_change_window_attributes(this.xConnection
													.getConnectionReference(),
											windowId.intValue(),
											xcb_cw_t.XCB_CW_EVENT_MASK
													.swigValue(),
											values);
	}
}

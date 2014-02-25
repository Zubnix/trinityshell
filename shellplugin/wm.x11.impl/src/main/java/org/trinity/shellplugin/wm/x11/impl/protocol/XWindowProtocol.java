/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shellplugin.wm.x11.impl.protocol;

import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_window_attributes_cookie_t;
import org.freedesktop.xcb.xcb_get_window_attributes_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.display.x11.api.XEventChannel;
import org.trinity.foundation.display.x11.api.XcbErrorUtil;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE;

@ThreadSafe
@Singleton
public class XWindowProtocol {

	private static final Logger     LOG           = LoggerFactory.getLogger(XWindowProtocol.class);
	private static final ByteBuffer PROPERTY_MASK = allocateDirect(4).order(nativeOrder());
	private final XEventChannel xEventChannel;

	@Inject
	XWindowProtocol(
			final XEventChannel xEventChannel) {
		this.xEventChannel = xEventChannel;
	}

	public void register(final DisplaySurface xWindow) {
		final Integer xWindowId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		listenForXProtocol(xWindowId);
	}

	private void listenForXProtocol(final Integer xWindowId) {
		final xcb_get_window_attributes_cookie_t get_window_attributes_cookie = xcb_get_window_attributes(this.xEventChannel
																												  .getConnectionReference(),
																										  xWindowId);
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_get_window_attributes_reply_t get_window_attributes_reply = xcb_get_window_attributes_reply(this.xEventChannel
																													  .getConnectionReference(),
																											  get_window_attributes_cookie,
																											  e);
		if(xcb_generic_error_t.getCPtr(e) != 0) {
			LOG.error("Got X error while querying window attributes. Window property changes will not be propagated!\n {}",
					  XcbErrorUtil.toString(e));
			return;
		}

		final int updatedEventMask = get_window_attributes_reply.getYour_event_mask() | XCB_EVENT_MASK_PROPERTY_CHANGE;
		PROPERTY_MASK.clear();
		PROPERTY_MASK.putInt(updatedEventMask);

		xcb_change_window_attributes(this.xEventChannel.getConnectionReference(),
									 xWindowId,
									 XCB_CW_EVENT_MASK,
                                     PROPERTY_MASK);
        xcb_flush(this.xEventChannel.getConnectionReference());
    }
}

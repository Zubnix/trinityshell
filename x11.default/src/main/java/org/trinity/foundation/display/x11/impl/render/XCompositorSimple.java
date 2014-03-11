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
package org.trinity.foundation.display.x11.impl.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.display.x11.impl.XCompositor;
import org.trinity.foundation.display.x11.impl.XEventChannel;
import org.trinity.foundation.display.x11.impl.XWindow;
import org.trinity.foundation.display.x11.impl.XWindowFactory;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_ENTER_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_LEAVE_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_STRUCTURE_NOTIFY;


@Singleton
@ThreadSafe
public class XCompositorSimple implements XCompositor {

    private static final Logger LOG = LoggerFactory.getLogger(XCompositorSimple.class);

    private static final int CLIENT_EVENT_MASK = XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW | XCB_EVENT_MASK_STRUCTURE_NOTIFY;
    private static final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder()).putInt(CLIENT_EVENT_MASK);

    private final XEventChannel xEventChannel;
    private final XWindowFactory xWindowFactory;

    @Inject
    XCompositorSimple(final XEventChannel xEventChannel,
                      final XWindowFactory xWindowFactory) {
        this.xEventChannel = xEventChannel;
        this.xWindowFactory = xWindowFactory;
    }

    @Override
	public XWindow createSurface(final Integer nativeHandle) {
        configureClientEvents(nativeHandle);
		return this.xWindowFactory.create(nativeHandle);
    }

    private void configureClientEvents(final Integer nativeHandle) {

        LOG.debug("[winId={}] configure client evens.",
                nativeHandle);

        xcb_change_window_attributes(this.xEventChannel.getXcbConnection(),
                nativeHandle,
                XCB_CW_EVENT_MASK,
                CLIENT_EVENTS_CONFIG_BUFFER);
        xcb_flush(this.xEventChannel.getXcbConnection());
    }
}
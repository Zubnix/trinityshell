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
package org.trinity.foundation.display.x11.impl;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.xcb.xcb_destroy_notify_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

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
public class XWindowPool {

    private static final Logger LOG = LoggerFactory.getLogger(XWindowPool.class);

    private static final int CLIENT_EVENT_MASK = XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW | XCB_EVENT_MASK_STRUCTURE_NOTIFY;
    private static final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder()).putInt(CLIENT_EVENT_MASK);

    private final Map<Integer, XWindow> XWindows = new HashMap<>();

    private final XEventChannel xEventChannel;
    private final XCompositor compositor;

    @Inject
    XWindowPool(final XEventChannel xEventChannel,
                       final XCompositor compositor) {
        this.xEventChannel = xEventChannel;
        this.compositor = compositor;
    }

    public XWindow get(final Integer xWindowHandle) {

        XWindow window = this.XWindows.get(xWindowHandle);
        if (window == null) {
            window = registerNewXWindow(xWindowHandle);
            configureClientEvents(window);
        }
        return window;
    }

    private XWindow registerNewXWindow(final Integer xWindowHandle) {
        LOG.debug("Xwindow={} added to cache.",
                xWindowHandle);

        final XWindow window = this.compositor.createSurface(xWindowHandle);
        window.register(new DestroyListener(window));
        this.XWindows.put(xWindowHandle,
                window);
        return window;
    }

    private void configureClientEvents(final XWindow window) {
        final int winId = (Integer) window.getNativeHandle();

        LOG.debug("[winId={}] configure client evens.",
                winId);

        xcb_change_window_attributes(this.xEventChannel.getXcbConnection(),
                winId,
                XCB_CW_EVENT_MASK,
                CLIENT_EVENTS_CONFIG_BUFFER);
        xcb_flush(this.xEventChannel.getXcbConnection());
    }

    private void unregisterXWindow(final XWindow xWindow) {
        this.XWindows.remove(xWindow.getNativeHandle().hashCode());
        xWindow.unregister(this);
    }

    private class DestroyListener {
        private final XWindow window;

        public DestroyListener(final XWindow XWindow) {
            this.window = XWindow;
        }

        @Subscribe
        public void destroyed(final xcb_destroy_notify_event_t destroyNotifyEvent) {
            unregisterXWindow(this.window);
        }
    }
}
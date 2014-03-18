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
package org.trinity.x11.defaul;

import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_screen_iterator_t;
import org.freedesktop.xcb.xcb_screen_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.common.Listenable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_connect;
import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_disconnect;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_get_setup;
import static org.freedesktop.xcb.LibXcb.xcb_screen_next;
import static org.freedesktop.xcb.LibXcb.xcb_setup_roots_iterator;
import static org.freedesktop.xcb.LibXcb.xcb_wait_for_event;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT;

@Singleton
@NotThreadSafe
public class XEventChannel extends EventBus implements Listenable {

    private static final Logger LOG = LoggerFactory.getLogger(XEventChannel.class);

    private final ExecutorService eventPumpThread = Executors.newSingleThreadExecutor();
    private final ByteBuffer rootWindowAttributes = allocateDirect(4).order(nativeOrder()).putInt(XCB_EVENT_MASK_PROPERTY_CHANGE | XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT);

    private SWIGTYPE_p_xcb_connection_t xcb_connection;
    private xcb_screen_t xcb_screen;

    @Inject
    XEventChannel() {
    }

    public void open(@Nonnull final String displayName,
                     @Nonnegative final Integer screen) {
        checkNotNull(displayName);

        final ByteBuffer screenBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
        screenBuf.putInt(screen);
        this.xcb_connection = xcb_connect(displayName,
                screenBuf);
        if (xcb_connection_has_error(getXcbConnection()) != 0) {
            throw new Error("Cannot open display\n");
        }
        // FIXME from config?
        final int targetScreen = 0;

        final xcb_screen_iterator_t iter = xcb_setup_roots_iterator(xcb_get_setup(getXcbConnection()));
        int screenNr;
        for (; iter.getRem() != 0; --screenNr, xcb_screen_next(iter)) {
            if (targetScreen == 0) {
                this.xcb_screen = iter.getData();
                configureRootEvents(getXcbScreen());
                break;
            }
        }
    }

    public xcb_screen_t getXcbScreen() {
        return this.xcb_screen;
    }

    private void configureRootEvents(final xcb_screen_t xcb_screen) {
        final int rootId = xcb_screen.getRoot();

        xcb_change_window_attributes(getXcbConnection(),
                rootId,
                XCB_CW_EVENT_MASK,
                this.rootWindowAttributes);
        xcb_flush(getXcbConnection());
    }

    public void close() {
        xcb_disconnect(this.xcb_connection);
    }

    public SWIGTYPE_p_xcb_connection_t getXcbConnection() {
        return this.xcb_connection;
    }

    public void pump() {
        if (xcb_connection_has_error(getXcbConnection()) != 0) {
            final String errorMsg = "X11 connection was closed unexpectedly - maybe your X server terminated / crashed?";
            LOG.error(errorMsg);
            throw new Error(errorMsg);
        }

        final xcb_generic_event_t xcb_generic_event = xcb_wait_for_event(getXcbConnection());

        post(xcb_generic_event);
        xcb_generic_event.delete();
    }
}

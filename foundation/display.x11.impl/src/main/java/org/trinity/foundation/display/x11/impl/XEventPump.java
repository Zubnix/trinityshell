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

import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_wait_for_event;

@Singleton
@NotThreadSafe
public class XEventPump implements Callable<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(XEventPump.class);
    private final XConnection connection;
    private final EventBus xEventBus;

    private final Semaphore waitForExternal = new Semaphore(1);

    @Inject
    XEventPump(final XConnection connection,
               @XEventBus final EventBus xEventBus) {
        this.connection = connection;
        this.xEventBus = xEventBus;
    }

    @Override
    public Void call() {

        if(xcb_connection_has_error(this.connection.getConnectionReference()) != 0) {
            final String errorMsg = "X11 connection was closed unexpectedly - maybe your X server terminated / crashed?";
            LOG.error(errorMsg);
            throw new Error(errorMsg);
        }

        final xcb_generic_event_t xcb_generic_event = xcb_wait_for_event(this.connection.getConnectionReference());

        try {
			this.waitForExternal.acquireUninterruptibly();
            //TODO use channel
//            this.xExecutor.submit(new Callable<Void>() {
//                @Override
//                public Void call() {
//                    XEventPump.this.xEventBus.post(xcb_generic_event);
//                    xcb_generic_event.delete();
//                    return null;
//                }
//            });

            // schedule next event retrieval
          //  this.xEventPumpExecutor.submit(this);
        } finally {
			this.waitForExternal.release();
        }
        return null;
    }

    public synchronized void start() {
		this.waitForExternal.release();
    }

    public synchronized void stop() {
		this.waitForExternal.acquireUninterruptibly();
    }
}

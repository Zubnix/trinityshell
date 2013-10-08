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
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.bindkey.XEventBus;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_wait_for_event;

@Bind(to = @To(IMPLEMENTATION))
@Singleton
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public class XEventPump implements Callable<Void> {

    private static final Logger LOG = LoggerFactory.getLogger(XEventPump.class);
    private final XConnection connection;
    private final EventBus xEventBus;
    private final ListeningExecutorService xExecutor;
    private final ExecutorService xEventPumpExecutor = newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r,
                    "x-event-pump");
        }
    });

    private final Semaphore waitForExternal = new Semaphore(1);

    @Inject
    XEventPump(final XConnection connection,
               @XEventBus final EventBus xEventBus,
               @DisplayExecutor final ListeningExecutorService xExecutor) {
        this.connection = connection;
        this.xEventBus = xEventBus;
        this.xExecutor = xExecutor;
        this.xEventPumpExecutor.submit(this);
    }

    @Override
    public Void call() {

        final xcb_generic_event_t xcb_generic_event = xcb_wait_for_event(this.connection.getConnectionReference()
                .get());

        try {
            waitForExternal.acquireUninterruptibly();

            if (xcb_connection_has_error(this.connection.getConnectionReference().get()) != 0) {
                final String errorMsg = "X11 connection was closed unexpectedly - maybe your X server terminated / crashed?";
                XEventPump.LOG.error(errorMsg);
                throw new Error(errorMsg);
            }

            // pass x event from x-event-pump thread to x-executor thread.

            this.xExecutor.submit(new Callable<Void>() {
                @Override
                public Void call() {
                    XEventPump.this.xEventBus.post(xcb_generic_event);
                    xcb_generic_event.delete();
                    return null;
                }
            });

            // schedule next event retrieval
            this.xEventPumpExecutor.submit(this);
        } finally {
            waitForExternal.release();
        }
        return null;
    }

    public synchronized void start() {
        waitForExternal.release();
    }

    public synchronized void stop() {
        waitForExternal.acquireUninterruptibly();
    }
}

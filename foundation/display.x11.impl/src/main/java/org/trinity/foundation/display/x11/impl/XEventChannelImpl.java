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

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.trinity.foundation.api.shared.ListenableEventBus;
import org.trinity.foundation.display.x11.api.XEventChannel;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.freedesktop.xcb.LibXcb.xcb_connect;
import static org.freedesktop.xcb.LibXcb.xcb_disconnect;

@Singleton
@NotThreadSafe
public class XEventChannelImpl extends ListenableEventBus implements XEventChannel {

    private final ExecutorService eventPumpThread = Executors.newSingleThreadExecutor();

	private SWIGTYPE_p_xcb_connection_t xcb_connection;

	private final Semaphore postLock = new Semaphore(1);

	@Inject
	XEventChannelImpl() {
		// FIXME from config?
		final String displayName = System.getenv("DISPLAY");
		final int targetScreen = 0;

		open(displayName,
			 targetScreen);
	}

	private void open(@Nonnull final String displayName,
					  @Nonnegative final Integer screen) {
		checkNotNull(displayName);

		final ByteBuffer screenBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
		screenBuf.putInt(screen);
		this.xcb_connection = xcb_connect(displayName,
										  screenBuf);
        pump();
	}

	@Override
	public void close() {
		xcb_disconnect(this.xcb_connection);
	}

	@Override
	public SWIGTYPE_p_xcb_connection_t getConnectionReference() {
		return this.xcb_connection;
	}

	@Override
	public void start() {
		this.postLock.release();
	}

	@Override
	public void stop() {
		this.postLock.acquireUninterruptibly();
	}

    private void pump() {
        this.eventPumpThread.submit(new Runnable() {
            @Override
            public void run() {
                if(xcb_connection_has_error(this.xEventChannel.getConnectionReference()) != 0) {
                    final String errorMsg = "X11 connection was closed unexpectedly - maybe your X server terminated / crashed?";
                    LOG.error(errorMsg);
                    throw new Error(errorMsg);
                }

                final xcb_generic_event_t xcb_generic_event = xcb_wait_for_event(this.xEventChannel.getConnectionReference());

                 //acquire permit
                 stop();

                 post(xcb_generic_event);
                 xcb_generic_event.delete();

                 //release permit
                 start();
                 pump();
            }
        });
    }
}

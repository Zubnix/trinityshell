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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.freedesktop.xcb.LibXcb.xcb_connect;
import static org.freedesktop.xcb.LibXcb.xcb_disconnect;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;

import com.google.common.base.Optional;
import com.google.inject.Singleton;

@Bind
@Singleton
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public class XConnectionImpl implements XConnection {

    private SWIGTYPE_p_xcb_connection_t xcb_connection;

    XConnectionImpl() {
    }

    @Override
    public void open(@Nonnull final String displayName,
                     @Nonnegative final int screen) {
        checkNotNull(displayName);

        final ByteBuffer screenBuf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder());
        screenBuf.putInt(screen);
        this.xcb_connection = xcb_connect(displayName,
                screenBuf);
    }

    @Override
    public void close() {
        xcb_disconnect(this.xcb_connection);
    }

    @Override
    public Optional<SWIGTYPE_p_xcb_connection_t> getConnectionReference() {
        return Optional.fromNullable(this.xcb_connection);
    }
}

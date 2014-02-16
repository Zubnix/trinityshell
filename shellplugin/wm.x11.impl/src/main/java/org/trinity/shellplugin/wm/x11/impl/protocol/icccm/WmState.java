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

package org.trinity.shellplugin.wm.x11.impl.protocol.icccm;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_get_property;
import static org.freedesktop.xcb.LibXcb.xcb_get_property_reply;
import static org.freedesktop.xcb.LibXcb.xcb_get_property_value;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_property_cookie_t;
import org.freedesktop.xcb.xcb_get_property_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XcbErrorUtil;
import org.trinity.shellplugin.wm.x11.impl.protocol.XAtomCache;

import com.google.common.base.Optional;

@Bind(to = @To(IMPLEMENTATION))
@Singleton
@NotThreadSafe
public class WmState extends AbstractCachedProtocol<int[]> {

	private static final Logger LOG = LoggerFactory.getLogger(WmState.class);
	private final XConnection xConnection;

	@Inject
	WmState(
			final XConnection xConnection,
			final XAtomCache xAtomCache) {
		super(
				xAtomCache,
				"WM_STATE");
		this.xConnection = xConnection;
	}

	@Override
	protected Optional<int[]> queryProtocol(final DisplaySurface xWindow) {

		final Integer winId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
		final xcb_get_property_cookie_t get_wm_state_cookie = xcb_get_property(	this.xConnection
																						.getConnectionReference(),
																				(short) 0,
                                                                                winId,
																				getProtocolAtomId(),
																				getProtocolAtomId(),
																				0,
																				2);
        final xcb_generic_error_t e = new xcb_generic_error_t();
        final int[] reply = new int[2];

        final xcb_get_property_reply_t get_wm_state_reply = xcb_get_property_reply(	WmState.this.xConnection
                .getConnectionReference(),
                get_wm_state_cookie,
                e);
        if (xcb_generic_error_t.getCPtr(e) != 0) {
            final String errorString = XcbErrorUtil.toString(e);
            LOG.error(errorString);
            return Optional.absent();
        }
        if (get_wm_state_reply.getLength() == 0) {
            return Optional.absent();
        }
        final ByteBuffer wm_state_property_value = xcb_get_property_value(get_wm_state_reply).order(ByteOrder
                .nativeOrder());
        reply[0] = wm_state_property_value.getInt();
        reply[1] = wm_state_property_value.getInt();

        return Optional.of(reply);
	}

}

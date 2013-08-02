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

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_get_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_get_window_attributes_reply;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_window_attributes_cookie_t;
import org.freedesktop.xcb.xcb_get_window_attributes_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XcbErrorUtil;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

@ThreadSafe
@Bind(to = @To(IMPLEMENTATION))
@Singleton
@ExecutionContext(DisplayExecutor.class)
public class XWindowProtocol {

	private static final Logger LOG = LoggerFactory.getLogger(XWindowProtocol.class);
	private static final ByteBuffer PROPERTY_MASK = allocateDirect(4).order(nativeOrder());
	private final ListeningExecutorService displayExecutor;
	private final XConnection xConnection;

	@Inject
	XWindowProtocol(@DisplayExecutor ListeningExecutorService displayExecutor,
					final XConnection xConnection) {
		this.displayExecutor = displayExecutor;
		this.xConnection = xConnection;
	}

	public ListenableFuture<Void> register(final DisplaySurface xWindow) {
		return displayExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				final Integer xWindowId = (Integer) xWindow.getDisplaySurfaceHandle().getNativeHandle();
				listenForXProtocol(xWindowId);
				return null;
			}
		});
	}

	private void listenForXProtocol(final Integer xWindowId) {
		final xcb_get_window_attributes_cookie_t get_window_attributes_cookie = xcb_get_window_attributes(	this.xConnection
																													.getConnectionReference()
																													.get(),
																											xWindowId
																													.intValue());
		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_get_window_attributes_reply_t get_window_attributes_reply = xcb_get_window_attributes_reply(	this.xConnection
																														.getConnectionReference()
																														.get(),
																												get_window_attributes_cookie,
																												e);
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			LOG.error(	"Got X error while querying window attributes. Window property changes will not be propagated!\n {}",
						XcbErrorUtil.toString(e));
			return;
		}

		final int updatedEventMask = get_window_attributes_reply.getAll_event_masks() | XCB_EVENT_MASK_PROPERTY_CHANGE;
		PROPERTY_MASK.putInt(	0,
								updatedEventMask);

		xcb_change_window_attributes(	this.xConnection.getConnectionReference().get(),
										xWindowId.intValue(),
										XCB_CW_EVENT_MASK,
										PROPERTY_MASK);
		xcb_flush(this.xConnection.getConnectionReference().get());
	}
}

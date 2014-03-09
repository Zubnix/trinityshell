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

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_geometry_cookie_t;
import org.freedesktop.xcb.xcb_get_geometry_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.shared.ListenableEventBus;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.media.nativewindow.util.Rectangle;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_config_window_t.*;

@ThreadSafe
@AutoFactory
public class XWindow implements DisplaySurface {

	private static final Logger LOG = LoggerFactory.getLogger(XWindow.class);

	private static final int        MOVE_RESIZE_VALUE_MASK        = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y | XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT;
	private static final ByteBuffer MOVE_RESIZE_VALUE_LIST_BUFFER = allocateDirect(16).order(nativeOrder());

	private final DisplaySurfaceHandle resourceHandle;
	private final XEventChannel        xEventChannel;
	private final ListenableEventBus   xWindowEventBus;

	XWindow(@Provided final XEventChannel xEventChannel,
			@Nonnull final DisplaySurfaceHandle resourceHandle) {
		checkNotNull(resourceHandle);

		this.xEventChannel = xEventChannel;
		this.resourceHandle = resourceHandle;
		this.xWindowEventBus = new ListenableEventBus();
	}

	@Override
	public void register(@Nonnull final Object listener) {
		this.xWindowEventBus.register(listener);
	}

	@Override
	public void post(@Nonnull final Object event) {
		this.xWindowEventBus.post(event);
	}

	@Override
	public void unregister(@Nonnull final Object listener) {
		this.xWindowEventBus.unregister(listener);
	}

	@Override
	public DisplaySurfaceHandle getDisplaySurfaceHandle() {
		return this.resourceHandle;
	}

	@Override
	public void destroy() {
		final int winId = getWindowId();
		LOG.debug("[winId={}] destroy.",
				  winId);
		xcb_destroy_window(getConnectionRef(),
						   winId);
		xcb_flush(getConnectionRef());
	}

	private int getWindowId() {
		return ((Number) this.resourceHandle.getNativeHandle()).intValue();
	}

	private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		return this.xEventChannel.getXcbConnection();
	}

	@Override
	public void configure(final int x,
						  final int y,
						  final int width,
						  final int height) {

		// we have to adjust the size with the X border. This sucks because it
		// introduces an extra roundtrip to the X server. -_-

		final int winId = getWindowId();
		final xcb_get_geometry_cookie_t cookie_t = xcb_get_geometry(getConnectionRef(),
																	winId);


		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_get_geometry_reply_t reply = xcb_get_geometry_reply(getConnectionRef(),
																	  cookie_t,
																	  e);

		checkError(e);
		final Integer border = reply.getBorder_width();


		final int borderAdjust = 2 * border;
		final int adjustedWidth = width - borderAdjust;
		final int adjustedHeight = height - borderAdjust;

		MOVE_RESIZE_VALUE_LIST_BUFFER.clear();
		MOVE_RESIZE_VALUE_LIST_BUFFER.putInt(x).putInt(y).putInt(adjustedWidth)
									 .putInt(adjustedHeight);

		LOG.debug("[winId={}] move resize x={}, y={}, width={}, height={}.",
				  winId,
				  x,
				  y,
				  adjustedWidth,
				  adjustedHeight);
		xcb_configure_window(getConnectionRef(),
							 winId,
							 XWindow.MOVE_RESIZE_VALUE_MASK,
							 XWindow.MOVE_RESIZE_VALUE_LIST_BUFFER);
		xcb_flush(getConnectionRef());
	}

	@Override
	public Rectangle getShape() {
		final int winId = getWindowId();

		final xcb_get_geometry_cookie_t geometryRequest = xcb_get_geometry(getConnectionRef(),
																		   winId);

		LOG.debug("get geometry reply.");

		final xcb_generic_error_t e = new xcb_generic_error_t();
		final xcb_get_geometry_reply_t get_geometry_reply = xcb_get_geometry_reply(getConnectionRef(),
																				   geometryRequest,
																				   e);

		checkError(e);
		final int width = get_geometry_reply.getWidth() + (2 * get_geometry_reply.getBorder_width());
		final int height = get_geometry_reply.getHeight() + (2 * get_geometry_reply.getBorder_width());
		final int x = get_geometry_reply.getX();
		final int y = get_geometry_reply.getY();

		return new Rectangle(x,
							 y,
							 width,
							 height);
	}

	@Override
	public Rectangle getInputRegion() {
		//by default we return the entire client's shape
		return getShape();
	}

	private void checkError(final xcb_generic_error_t e) {
		if(xcb_generic_error_t.getCPtr(e) != 0) {
			LOG.error("X error: {}.",
					  XcbErrorUtil.toString(e));
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if(obj instanceof XWindow) {
			final XWindow otherWindow = (XWindow) obj;
			return otherWindow.getDisplaySurfaceHandle()
							  .equals(getDisplaySurfaceHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getDisplaySurfaceHandle().hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s=%s",
							 getClass().getSimpleName(),
							 getDisplaySurfaceHandle().getNativeHandle());
	}
}
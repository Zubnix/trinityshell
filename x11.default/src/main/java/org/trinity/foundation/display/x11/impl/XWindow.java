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
import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_geometry_cookie_t;
import org.freedesktop.xcb.xcb_get_geometry_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.common.Listenable;
import org.trinity.shell.scene.api.SpaceBuffer;
import org.trinity.shell.scene.api.HasSize;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.Rectangle;
import javax.media.nativewindow.util.RectangleImmutable;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_config_window_t.*;

@ThreadSafe
@AutoFactory
public class XWindow extends EventBus implements Listenable, HasSize<SpaceBuffer> {

	private static final Logger LOG = LoggerFactory.getLogger(XWindow.class);

	private static final int        MOVE_RESIZE_VALUE_MASK        = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y | XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT;
	private static final ByteBuffer MOVE_RESIZE_VALUE_LIST_BUFFER = allocateDirect(16).order(nativeOrder());

	private final Integer       nativeHandle;
	private final XEventChannel xEventChannel;

	XWindow(@Provided final XEventChannel xEventChannel,
			@Nonnull final Integer nativeHandle) {
		checkNotNull(nativeHandle);

		this.xEventChannel = xEventChannel;
		this.nativeHandle = nativeHandle;
	}

	public Integer getNativeHandle() {
		return this.nativeHandle;
	}

	public void destroy() {
		final int winId = getNativeHandle();
		LOG.debug("[winId={}] destroy.",
				  winId);
		xcb_destroy_window(getConnectionRef(),
						   winId);
		xcb_flush(getConnectionRef());
	}

	private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		return this.xEventChannel.getXcbConnection();
	}

	public void configure(final int x,
						  final int y,
						  final int width,
						  final int height) {

		// we have to adjust the size with the X border. This sucks because it
		// introduces an extra roundtrip to the X server. -_-
		//TODO keep track of the size & border through event listeners?

		final int winId = getNativeHandle();
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

	public RectangleImmutable getShape() {
		//TODO keep track of the size & border through event listeners?

		final int winId = getNativeHandle();

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
	public DimensionImmutable getSize() {
		final RectangleImmutable shape = getShape();
		return new Dimension(shape.getWidth(),
							 shape.getHeight());
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
			return otherWindow.getNativeHandle()
							  .equals(getNativeHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getNativeHandle();
	}

	@Override
	public String toString() {
		return String.format("%s=%s",
							 getClass().getSimpleName(),
							 getNativeHandle());
	}
}
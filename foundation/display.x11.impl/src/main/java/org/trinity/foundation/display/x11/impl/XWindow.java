/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.x11.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.Callable;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_config_window_t;
import org.freedesktop.xcb.xcb_cw_t;
import org.freedesktop.xcb.xcb_event_mask_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_geometry_cookie_t;
import org.freedesktop.xcb.xcb_get_geometry_reply_t;
import org.freedesktop.xcb.xcb_input_focus_t;
import org.freedesktop.xcb.xcb_stack_mode_t;
import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.shared.ImmutableRectangle;
import org.trinity.foundation.api.shared.Rectangle;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

public class XWindow implements DisplaySurface {

	private final DisplaySurfaceHandle resourceHandle;
	private final XConnection xConnection;
	private final XTime xTime;
	private final ListeningExecutorService xExecutor;

	@Inject
	XWindow(final XTime xTime, final XConnection xConnection,
			@Assisted final DisplaySurfaceHandle resourceHandle,
			@Named("XExecutor") ListeningExecutorService xExecutor) {
		this.xTime = xTime;
		this.xConnection = xConnection;
		this.resourceHandle = resourceHandle;
		this.xExecutor = xExecutor;
	}

	@Override
	public DisplaySurfaceHandle getDisplaySurfaceHandle() {
		return this.resourceHandle;
	}

	@Override
	public ListenableFuture<Void> destroy() {
		final int winId = getWindowId();
		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_destroy_window(getConnectionRef(), winId);
			}
		}, null);
	}

	private int getWindowId() {
		final int windowId = ((Integer) this.resourceHandle.getNativeHandle())
				.intValue();
		return windowId;
	}

	private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		final SWIGTYPE_p_xcb_connection_t connection_t = this.xConnection
				.getConnectionReference();
		return connection_t;
	}

	@Override
	public ListenableFuture<Void> setInputFocus() {

		final int winId = getWindowId();
		final int time = this.xTime.getTime();

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_set_input_focus(getConnectionRef(),
						(short) xcb_input_focus_t.XCB_INPUT_FOCUS_NONE, winId,
						time);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> lower() {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_STACK_MODE;
		final ByteBuffer value_list = ByteBuffer.allocateDirect(4).order(
				ByteOrder.nativeOrder());
		final int xcb_stack_mode_above = xcb_stack_mode_t.XCB_STACK_MODE_BELOW;
		value_list.putInt(xcb_stack_mode_above);
		final int winId = getWindowId();

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_configure_window(getConnectionRef(), winId,
						value_mask, value_list);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> show() {
		final int winId = getWindowId();

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {

				LibXcb.xcb_map_window(getConnectionRef(), winId);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> move(final int x, final int y) {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_X
				| xcb_config_window_t.XCB_CONFIG_WINDOW_Y;
		final ByteBuffer value_list = ByteBuffer.allocateDirect(8).order(
				ByteOrder.nativeOrder());
		value_list.putInt(x).putInt(y);
		final int winId = getWindowId();

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_configure_window(getConnectionRef(), winId,
						value_mask, value_list);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> moveResize(final int x, final int y,
			final int width, final int height) {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_X
				| xcb_config_window_t.XCB_CONFIG_WINDOW_Y
				| xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH
				| xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT;
		final ByteBuffer value_list = ByteBuffer.allocateDirect(16).order(
				ByteOrder.nativeOrder());
		value_list.putInt(x).putInt(y).putInt(width).putInt(height);
		final int winId = getWindowId();

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_configure_window(getConnectionRef(), winId,
						value_mask, value_list);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> raise() {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_STACK_MODE;
		final ByteBuffer value_list = ByteBuffer.allocateDirect(4).order(
				ByteOrder.nativeOrder());
		final int xcb_stack_mode_above = xcb_stack_mode_t.XCB_STACK_MODE_ABOVE;
		value_list.putInt(xcb_stack_mode_above);
		final int winId = getWindowId();

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_configure_window(getConnectionRef(), winId,
						value_mask, value_list);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> setParent(final DisplayArea parent,
			final int x, final int y) {

		final int parentId = ((Integer) (((DisplaySurface) parent)
				.getDisplaySurfaceHandle()).getNativeHandle()).intValue();
		final int winId = getWindowId();

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_reparent_window(getConnectionRef(), winId, parentId,
						(short) x, (short) y);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> resize(final int width, final int height) {

		final int value_mask = xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH
				| xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT;

		final ByteBuffer value_list = ByteBuffer.allocateDirect(8).order(
				ByteOrder.nativeOrder());
		value_list.putInt(width).putInt(height);
		final int winId = getWindowId();

		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_configure_window(getConnectionRef(), winId,
						value_mask, value_list);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> hide() {
		final int winId = getWindowId();
		return xExecutor.submit(new Runnable() {

			@Override
			public void run() {
				LibXcb.xcb_unmap_window(getConnectionRef(), winId);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Rectangle> getGeometry() {
		final int winId = getWindowId();

		return xExecutor.submit(new Callable<Rectangle>() {

			@Override
			public Rectangle call() {
				final xcb_get_geometry_cookie_t cookie_t = LibXcb
						.xcb_get_geometry(getConnectionRef(), winId);

				final xcb_generic_error_t e = new xcb_generic_error_t();
				final xcb_get_geometry_reply_t reply = LibXcb
						.xcb_get_geometry_reply(getConnectionRef(), cookie_t, e);
				checkError(e);
				final int width = reply.getWidth();
				final int height = reply.getHeight();
				final int x = reply.getX();
				final int y = reply.getY();

				return new ImmutableRectangle(x, y, width, height);
			}
		});

	}

	private void checkError(final xcb_generic_error_t e) {
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			// TODO logging
			System.err.println(XcbErrorUtil.toString(e));
			// Thread.dumpStack();
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof XWindow) {
			final XWindow otherWindow = (XWindow) obj;
			return otherWindow.getDisplaySurfaceHandle().getNativeHandle()
					.equals(getDisplaySurfaceHandle().getNativeHandle());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getDisplaySurfaceHandle().getNativeHandle().hashCode();
	}

	public void configureClientEvents() {

		final ByteBuffer values = ByteBuffer.allocateDirect(4).order(
				ByteOrder.nativeOrder());
		values.putInt(xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE //
				| xcb_event_mask_t.XCB_EVENT_MASK_ENTER_WINDOW //
				| xcb_event_mask_t.XCB_EVENT_MASK_LEAVE_WINDOW //
				| xcb_event_mask_t.XCB_EVENT_MASK_STRUCTURE_NOTIFY//
		);
		final int winId = getWindowId();

		xExecutor.submit(new Runnable() {
			@Override
			public void run() {
				LibXcb.xcb_change_window_attributes(
						xConnection.getConnectionReference(), winId,
						xcb_cw_t.XCB_CW_EVENT_MASK, values);
			}
		});
	}

	@Override
	public String toString() {
		return String.format("%s=%s", getClass().getSimpleName(),
				getDisplaySurfaceHandle().getNativeHandle());
	}
}
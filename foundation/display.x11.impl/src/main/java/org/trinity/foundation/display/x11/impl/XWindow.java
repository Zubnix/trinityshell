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
import static com.google.common.util.concurrent.Futures.transform;
import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.xcb_configure_window;
import static org.freedesktop.xcb.LibXcb.xcb_destroy_window;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_get_geometry;
import static org.freedesktop.xcb.LibXcb.xcb_get_geometry_reply;
import static org.freedesktop.xcb.LibXcb.xcb_map_window;
import static org.freedesktop.xcb.LibXcb.xcb_reparent_window;
import static org.freedesktop.xcb.LibXcb.xcb_set_input_focus;
import static org.freedesktop.xcb.LibXcb.xcb_unmap_window;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_HEIGHT;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_STACK_MODE;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_X;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_Y;
import static org.freedesktop.xcb.xcb_input_focus_t.XCB_INPUT_FOCUS_NONE;
import static org.freedesktop.xcb.xcb_stack_mode_t.XCB_STACK_MODE_ABOVE;
import static org.freedesktop.xcb.xcb_stack_mode_t.XCB_STACK_MODE_BELOW;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_geometry_cookie_t;
import org.freedesktop.xcb.xcb_get_geometry_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.AsyncListenableEventBus;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.ImmutableRectangle;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XcbErrorUtil;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

@ExecutionContext(DisplayExecutor.class)
@ThreadSafe
public final class XWindow implements DisplaySurface {

	private static final Logger LOG = LoggerFactory.getLogger(XWindow.class);
	private static final ByteBuffer MOVE_VALUE_LIST_BUFFER = allocateDirect(8).order(nativeOrder());
	private static final int MOVE_RESIZE_VALUE_MASK = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y
			| XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT;
	private static final ByteBuffer MOVE_RESIZE_VALUE_LIST_BUFFER = allocateDirect(16).order(nativeOrder());
	private static final int RESIZE_VALUE_MASK = XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT;
	private static final ByteBuffer RESIZE_VALUE_LIST = allocateDirect(8).order(nativeOrder());
	private static final int LOWER_VALUE_MASK = XCB_CONFIG_WINDOW_STACK_MODE;
	private static final ByteBuffer LOWER_VALUE_LIST_BUFFER = allocateDirect(4).order(nativeOrder())
			.putInt(XCB_STACK_MODE_BELOW);
	private static final int RAISE_VALUE_MASK = XCB_CONFIG_WINDOW_STACK_MODE;
	private static final ByteBuffer RAISE_VALUE_LIST_BUFFER = allocateDirect(4).order(nativeOrder())
			.putInt(XCB_STACK_MODE_ABOVE);
	private static final int MOVE_VALUE_MASK = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y;
	private final DisplaySurfaceHandle resourceHandle;
	private final XConnection xConnection;
	private final XTime xTime;
	private final ListeningExecutorService xExecutor;
	private final AsyncListenableEventBus xWindowEventBus;

	@AssistedInject
	XWindow(final XTime xTime,
			final XConnection xConnection,
			@Nonnull @Assisted final DisplaySurfaceHandle resourceHandle,
			@DisplayExecutor final ListeningExecutorService xExecutor) {
		checkNotNull(resourceHandle);

		this.xTime = xTime;
		this.xConnection = xConnection;
		this.resourceHandle = resourceHandle;
		this.xExecutor = xExecutor;
		this.xWindowEventBus = new AsyncListenableEventBus(xExecutor);
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
	public void register(@Nonnull	final Object listener,
	                     @Nonnull	final ExecutorService executor) {
		this.xWindowEventBus.register(	listener,
										executor);
	}

	@Override
	public DisplaySurfaceHandle getDisplaySurfaceHandle() {
		return this.resourceHandle;
	}

	@Override
	public ListenableFuture<Void> destroy() {
		final int winId = getWindowId();
		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												LOG.debug(	"[winId={}] destroy.",
															winId);
												xcb_destroy_window(	getConnectionRef(),
																	winId);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	private int getWindowId() {
		return (Integer) this.resourceHandle.getNativeHandle();
	}

	private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		return this.xConnection.getConnectionReference().get();
	}

	@Override
	public ListenableFuture<Void> setInputFocus() {

		final int winId = getWindowId();
		final int time = this.xTime.getTime();

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												LOG.debug(	"[winId={}] set input focus.",
															winId);
												xcb_set_input_focus(getConnectionRef(),
																	(short) XCB_INPUT_FOCUS_NONE,
																	winId,
																	time);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	public ListenableFuture<Void> lower() {

		final int winId = getWindowId();

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												LOG.debug(	"[winId={}] lower.",
															winId);
												xcb_configure_window(	getConnectionRef(),
																		winId,
																		XWindow.LOWER_VALUE_MASK,
																		XWindow.LOWER_VALUE_LIST_BUFFER);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override

	public ListenableFuture<Void> show() {
		final int winId = getWindowId();

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												LOG.debug(	"[winId={}] show.",
															winId);
												xcb_map_window(	getConnectionRef(),
																winId);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> move(	final int x,
										final int y) {

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												XWindow.MOVE_VALUE_LIST_BUFFER.clear();
												XWindow.MOVE_VALUE_LIST_BUFFER.putInt(x).putInt(y);
												final int winId = getWindowId();

												LOG.debug(	"[winId={}] move x={}, y={}.",
															x,
															y,
															winId);
												xcb_configure_window(	getConnectionRef(),
																		winId,
																		XWindow.MOVE_VALUE_MASK,
																		XWindow.MOVE_VALUE_LIST_BUFFER);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> moveResize(	final int x,
												final int y,
												final int width,
												final int height) {
		// we have to adjust the size with the X border. This sucks because it
		// introduces an extra roundtrip to the X server. -_-

		final int winId = getWindowId();
		final ListenableFuture<xcb_get_geometry_cookie_t> geometryRequest = this.xExecutor
				.submit(new Callable<xcb_get_geometry_cookie_t>() {
					@Override
					public xcb_get_geometry_cookie_t call() {
						LOG.debug("[winId={}] get geometry request.",
								winId);

						return xcb_get_geometry(getConnectionRef(),
								winId);
					}
				});

		final ListenableFuture<Integer> borderFuture = transform(	geometryRequest,
																	new Function<xcb_get_geometry_cookie_t, Integer>() {
																		@Override
																		public Integer apply(final xcb_get_geometry_cookie_t cookie_t) {
																			final xcb_generic_error_t e = new xcb_generic_error_t();
																			final xcb_get_geometry_reply_t reply = xcb_get_geometry_reply(	getConnectionRef(),
																																			cookie_t,
																																			e);

																			checkError(e);
																			return reply.getBorder_width();
																		}
																	},this.xExecutor);

		return transform(	borderFuture,
							new Function<Integer, Void>() {
								@Override
								public Void apply(final Integer border) {

									final int borderAdjust = 2 * border;
									final int adjustedWidth = width - borderAdjust;
									final int adjustedHeight = height - borderAdjust;

									MOVE_RESIZE_VALUE_LIST_BUFFER.clear();
									MOVE_RESIZE_VALUE_LIST_BUFFER.putInt(x).putInt(y).putInt(adjustedWidth)
											.putInt(adjustedHeight);
									final int winId = getWindowId();

									LOG.debug(	"[winId={}] move resize x={}, y={}, width={}, height={}.",
												winId,
												x,
												y,
												adjustedWidth,
												adjustedHeight);
									xcb_configure_window(	getConnectionRef(),
															winId,
															XWindow.MOVE_RESIZE_VALUE_MASK,
															XWindow.MOVE_RESIZE_VALUE_LIST_BUFFER);
									xcb_flush(getConnectionRef());
									return null;
								}
							},this.xExecutor);
	}

	@Override
	public ListenableFuture<Void> raise() {
		final int winId = getWindowId();

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												LOG.debug(	"[winId={}] raise.",
															winId);
												xcb_configure_window(	getConnectionRef(),
																		winId,
																		XWindow.RAISE_VALUE_MASK,
																		XWindow.RAISE_VALUE_LIST_BUFFER);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	public ListenableFuture<Void> setParent(final DisplaySurface parent,
											final int x,
											final int y) {

		final int parentId = (int) parent.getDisplaySurfaceHandle().getNativeHandle();
		final int winId = getWindowId();

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												LOG.debug(	"[winId={}] set parent parentId={}, x={}, y={}.",
															parentId,
															x,
															y,
															winId);

												xcb_reparent_window(getConnectionRef(),
																	winId,
																	parentId,
																	(short) x,
																	(short) y);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> resize(	final int width,
											final int height) {
		// we have to adjust the size with the X border. This sucks because it
		// introduces an extra roundtrip to the X server. -_-

		final int winId = getWindowId();
		final ListenableFuture<xcb_get_geometry_cookie_t> geometryRequest = this.xExecutor
				.submit(new Callable<xcb_get_geometry_cookie_t>() {
					@Override
					public xcb_get_geometry_cookie_t call() {
						LOG.debug("[winId={}] get geometry request.",
								winId);

						return xcb_get_geometry(getConnectionRef(),
								winId);
					}
				});

		final ListenableFuture<Integer> borderFuture = transform(	geometryRequest,
																	new Function<xcb_get_geometry_cookie_t, Integer>() {
																		@Override
																		public Integer apply(final xcb_get_geometry_cookie_t cookie_t) {
																			final xcb_generic_error_t e = new xcb_generic_error_t();
																			final xcb_get_geometry_reply_t reply = xcb_get_geometry_reply(	getConnectionRef(),
																																			cookie_t,
																																			e);

																			checkError(e);
																			return reply.getBorder_width();
																		}
																	});

		return transform(	borderFuture,
							new Function<Integer, Void>() {
								@Override
								public Void apply(final Integer border) {
									final int borderAdjust = 2 * border;
									final int adjustedWidth = width - borderAdjust;
									final int adjustedHeight = height - borderAdjust;

									LOG.debug(	"[winId={}] resize width={}, height={}.",
												adjustedWidth,
												adjustedHeight,
												winId);

									RESIZE_VALUE_LIST.clear();
									RESIZE_VALUE_LIST.putInt(adjustedWidth).putInt(adjustedHeight);
									xcb_configure_window(	getConnectionRef(),
															winId,
															RESIZE_VALUE_MASK,
															RESIZE_VALUE_LIST);
									xcb_flush(getConnectionRef());
									return null;
								}
							});
	}

	@Override
	@Deprecated
	public ListenableFuture<Void> hide() {
		final int winId = getWindowId();
		return this.xExecutor.submit(	new Runnable() {
											@Override
											public void run() {
												XWindow.LOG.debug(	"[winId={}] hide.",
																	winId);

												xcb_unmap_window(	getConnectionRef(),
																	winId);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Rectangle> getGeometry() {
		final int winId = getWindowId();

		final ListenableFuture<xcb_get_geometry_cookie_t> geometryRequest = this.xExecutor
				.submit(new Callable<xcb_get_geometry_cookie_t>() {
					@Override
					public xcb_get_geometry_cookie_t call() {
						LOG.debug("[winId={}] get geometry request.",
								winId);

						return xcb_get_geometry(getConnectionRef(),
								winId);
					}
				});

		return transform(	geometryRequest,
							new AsyncFunction<xcb_get_geometry_cookie_t, Rectangle>() {
								@Override
								public ListenableFuture<Rectangle> apply(final xcb_get_geometry_cookie_t input) {
									return getGeometryReply(input);
								}
							});
	}

	protected ListenableFuture<Rectangle> getGeometryReply(final xcb_get_geometry_cookie_t cookie_t) {
		return this.xExecutor.submit(new Callable<Rectangle>() {
			@Override
			public Rectangle call() {
				LOG.debug("get geometry reply.");

				final xcb_generic_error_t e = new xcb_generic_error_t();
				final xcb_get_geometry_reply_t get_geometry_reply = xcb_get_geometry_reply(	getConnectionRef(),
																							cookie_t,
																							e);

				checkError(e);
				final int width = get_geometry_reply.getWidth() + (2 * get_geometry_reply.getBorder_width());
				final int height = get_geometry_reply.getHeight() + (2 * get_geometry_reply.getBorder_width());
				final int x = get_geometry_reply.getX();
				final int y = get_geometry_reply.getY();

				return new ImmutableRectangle(	x,
												y,
												width,
												height);
			}
		});
	}

	private void checkError(final xcb_generic_error_t e) {
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			LOG.error(	"X error: {}.",
						XcbErrorUtil.toString(e));
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

	@Override
	public String toString() {
		return String.format(	"%s=%s",
								getClass().getSimpleName(),
								getDisplaySurfaceHandle().getNativeHandle());
	}
}

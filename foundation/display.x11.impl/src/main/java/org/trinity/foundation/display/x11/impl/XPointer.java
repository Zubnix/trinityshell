package org.trinity.foundation.display.x11.impl;

import static com.google.common.util.concurrent.Futures.transform;
import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_grab_button;
import static org.freedesktop.xcb.LibXcb.xcb_grab_pointer;
import static org.freedesktop.xcb.LibXcb.xcb_grab_pointer_reply;
import static org.freedesktop.xcb.LibXcb.xcb_query_pointer;
import static org.freedesktop.xcb.LibXcb.xcb_query_pointer_reply;
import static org.freedesktop.xcb.LibXcb.xcb_ungrab_button;
import static org.freedesktop.xcb.LibXcb.xcb_ungrab_pointer;

import java.util.concurrent.Callable;

import org.freedesktop.xcb.LibXcbConstants;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_event_mask_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_grab_mode_t;
import org.freedesktop.xcb.xcb_grab_pointer_cookie_t;
import org.freedesktop.xcb.xcb_query_pointer_cookie_t;
import org.freedesktop.xcb.xcb_query_pointer_reply_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.input.Button;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Pointer;
import org.trinity.foundation.api.shared.Coordinate;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import org.trinity.foundation.display.x11.api.XConnection;

@Bind
@Singleton
public class XPointer implements Pointer {

	private static final Logger logger = LoggerFactory.getLogger(XPointer.class);

	private final XConnection xConnection;
	private final XTime xTime;
	private final ListeningExecutorService xExecutor;

	@Inject
	XPointer(	final XConnection xConnection,
				final XTime xTime,
				@Named("Display") final ListeningExecutorService xExecutor) {
		this.xConnection = xConnection;
		this.xTime = xTime;
		this.xExecutor = xExecutor;
	}

	private int getWindowId(final DisplaySurface displaySurface) {
		final int windowId = ((Integer) displaySurface.getDisplaySurfaceHandle().getNativeHandle()).intValue();
		return windowId;
	}

	private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		final SWIGTYPE_p_xcb_connection_t connection_t = this.xConnection.getConnectionReference();
		return connection_t;
	}

	private void checkError(final xcb_generic_error_t e) {
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			XPointer.logger.error(XcbErrorUtil.toString(e));
		}
	}

	@Override
	public ListenableFuture<Void> grabButton(	final DisplaySurface displaySurface,
												final Button catchButton,
												final InputModifiers withModifiers) {

		final int buttonCode = catchButton.getButtonCode();
		final int modifiers = withModifiers.getInputModifiersState();
		final int event_mask = xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_PRESS
				| xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_RELEASE;
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int confine_to = LibXcbConstants.XCB_NONE;
		final int cursor = LibXcbConstants.XCB_NONE;
		final int winId = getWindowId(displaySurface);

		return this.xExecutor.submit(	new Runnable() {
											@Override
											public void run() {
												xcb_grab_button(getConnectionRef(),
																(short) 0,
																winId,
																event_mask,
																(short) pointer_mode,
																(short) keyboard_mode,
																confine_to,
																cursor,
																(short) buttonCode,
																modifiers);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> grabPointer(final DisplaySurface displaySurface) {

		final int event_mask = xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_PRESS
				| xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_RELEASE;
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int confine_to = LibXcbConstants.XCB_NONE;
		final int cursor = LibXcbConstants.XCB_NONE;
		final int winId = getWindowId(displaySurface);
		final int time = this.xTime.getTime();

		final ListenableFuture<xcb_grab_pointer_cookie_t> grabPointerFuture = this.xExecutor
				.submit(new Callable<xcb_grab_pointer_cookie_t>() {
					@Override
					public xcb_grab_pointer_cookie_t call() {
						final xcb_grab_pointer_cookie_t grab_pointer_cookie_t = xcb_grab_pointer(	getConnectionRef(),
																									(short) 0,
																									winId,
																									event_mask,
																									(short) pointer_mode,
																									(short) keyboard_mode,
																									confine_to,
																									cursor,
																									time);
						return grab_pointer_cookie_t;
					}
				});

		return transform(	grabPointerFuture,
							new AsyncFunction<xcb_grab_pointer_cookie_t, Void>() {
								@Override
								public ListenableFuture<Void> apply(final xcb_grab_pointer_cookie_t grab_pointer_cookie_t) {
									return grabPointerReply(grab_pointer_cookie_t);
								}
							});
	}

	protected ListenableFuture<Void> grabPointerReply(final xcb_grab_pointer_cookie_t grab_pointer_cookie_t) {
		return this.xExecutor.submit(new Callable<Void>() {
			@Override
			public Void call() {
				final xcb_generic_error_t e = new xcb_generic_error_t();
				// TODO check if grab was
				// successful and return boolean?
				xcb_grab_pointer_reply(	getConnectionRef(),
										grab_pointer_cookie_t,
										e);
				checkError(e);
				return null;
			}

		});
	}

	@Override
	public ListenableFuture<Void> ungrabPointer() {
		final int time = this.xTime.getTime();

		return this.xExecutor.submit(	new Runnable() {
											@Override
											public void run() {
												xcb_ungrab_pointer(	XPointer.this.xConnection.getConnectionReference(),
																	time);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> ungrabButton(	final DisplaySurface displaySurface,
												final Button likeButton,
												final InputModifiers withModifiers) {

		final int button = likeButton.getButtonCode();
		final int modifiers = withModifiers.getInputModifiersState();
		final int winId = getWindowId(displaySurface);

		return this.xExecutor.submit(	new Runnable() {
											@Override
											public void run() {
												xcb_ungrab_button(	getConnectionRef(),
																	(short) button,
																	winId,
																	modifiers);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Coordinate> getPointerCoordinate(final DisplaySurface displaySurface) {
		final int winId = getWindowId(displaySurface);

		return this.xExecutor.submit(new Callable<Coordinate>() {
			@Override
			public Coordinate call() {
				final xcb_query_pointer_cookie_t cookie_t = xcb_query_pointer(	getConnectionRef(),
																				winId);
				final xcb_generic_error_t e = new xcb_generic_error_t();
				final xcb_query_pointer_reply_t reply_t = xcb_query_pointer_reply(	getConnectionRef(),
																					cookie_t,
																					e);
				checkError(e);

				final int x = reply_t.getWin_x();
				final int y = reply_t.getWin_y();

				return new Coordinate(	x,
										y);
			}
		});
	}
}

package org.trinity.foundation.display.x11.impl;

import java.util.concurrent.Callable;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.LibXcbConstants;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_event_mask_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_grab_mode_t;
import org.freedesktop.xcb.xcb_grab_pointer_cookie_t;
import org.freedesktop.xcb.xcb_query_pointer_cookie_t;
import org.freedesktop.xcb.xcb_query_pointer_reply_t;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.input.Button;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Pointer;
import org.trinity.foundation.api.shared.Coordinate;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XPointer implements Pointer {
	private final XConnection xConnection;
	private final XTime xTime;
	private final ListeningExecutorService xExecutor;

	@Inject
	XPointer(XConnection xConnection, XTime xTime,
			@Named("XExecutor") ListeningExecutorService xExecutor) {
		this.xConnection = xConnection;
		this.xTime = xTime;
		this.xExecutor = xExecutor;
	}

	private int getWindowId(DisplaySurface displaySurface) {
		final int windowId = ((Integer) displaySurface
				.getDisplaySurfaceHandle().getNativeHandle()).intValue();
		return windowId;
	}

	private SWIGTYPE_p_xcb_connection_t getConnectionRef() {
		final SWIGTYPE_p_xcb_connection_t connection_t = this.xConnection
				.getConnectionReference();
		return connection_t;
	}

	private void checkError(final xcb_generic_error_t e) {
		if (xcb_generic_error_t.getCPtr(e) != 0) {
			// TODO logging
			System.err.println(XcbErrorUtil.toString(e));
			// Thread.dumpStack();
		}
	}

	@Override
	public ListenableFuture<Void> grabButton(DisplaySurface displaySurface,
			final Button catchButton, final InputModifiers withModifiers) {

		final int buttonCode = catchButton.getButtonCode();
		final int modifiers = withModifiers.getInputModifiersState();
		final int event_mask = xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_PRESS
				| xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_RELEASE;
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int confine_to = LibXcbConstants.XCB_NONE;
		final int cursor = LibXcbConstants.XCB_NONE;
		final int winId = getWindowId(displaySurface);

		return this.xExecutor.submit(new Runnable() {
			@Override
			public void run() {
				LibXcb.xcb_grab_button(getConnectionRef(), (short) 0, winId,
						event_mask, (short) pointer_mode,
						(short) keyboard_mode, confine_to, cursor,
						(short) buttonCode, modifiers);

			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> grabPointer(DisplaySurface displaySurface) {

		final int event_mask = xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_PRESS
				| xcb_event_mask_t.XCB_EVENT_MASK_BUTTON_RELEASE;
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int confine_to = LibXcbConstants.XCB_NONE;
		final int cursor = LibXcbConstants.XCB_NONE;
		final int winId = getWindowId(displaySurface);
		final int time = this.xTime.getTime();

		return this.xExecutor.submit(new Runnable() {
			@Override
			public void run() {
				final xcb_grab_pointer_cookie_t grab_pointer_cookie_t = LibXcb
						.xcb_grab_pointer(getConnectionRef(), (short) 0, winId,
								event_mask, (short) pointer_mode,
								(short) keyboard_mode, confine_to, cursor, time);
				final xcb_generic_error_t e = new xcb_generic_error_t();
				// TODO check if grab was successful and return boolean
				LibXcb.xcb_grab_pointer_reply(getConnectionRef(),
						grab_pointer_cookie_t, e);
				checkError(e);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> ungrabPointer() {
		final int time = this.xTime.getTime();

		return this.xExecutor.submit(new Runnable() {
			@Override
			public void run() {
				LibXcb.xcb_ungrab_pointer(xConnection.getConnectionReference(),
						time);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Void> ungrabButton(DisplaySurface displaySurface,
			final Button likeButton, final InputModifiers withModifiers) {

		final int button = likeButton.getButtonCode();
		final int modifiers = withModifiers.getInputModifiersState();
		final int winId = getWindowId(displaySurface);

		return this.xExecutor.submit(new Runnable() {
			@Override
			public void run() {
				LibXcb.xcb_ungrab_button(getConnectionRef(), (short) button,
						winId, modifiers);
			}
		}, null);
	}

	@Override
	public ListenableFuture<Coordinate> getPointerCoordinate(
			DisplaySurface displaySurface) {
		final int winId = getWindowId(displaySurface);

		return this.xExecutor.submit(new Callable<Coordinate>() {
			@Override
			public Coordinate call() {
				final xcb_query_pointer_cookie_t cookie_t = LibXcb
						.xcb_query_pointer(getConnectionRef(), winId);
				final xcb_generic_error_t e = new xcb_generic_error_t();
				final xcb_query_pointer_reply_t reply_t = LibXcb
						.xcb_query_pointer_reply(getConnectionRef(), cookie_t,
								e);
				checkError(e);

				final int x = reply_t.getWin_x();
				final int y = reply_t.getWin_y();

				return new Coordinate(x, y);
			}
		});
	}
}

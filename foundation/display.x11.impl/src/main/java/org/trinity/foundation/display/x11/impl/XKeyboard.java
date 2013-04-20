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

import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_grab_key;
import static org.freedesktop.xcb.LibXcb.xcb_grab_keyboard;
import static org.freedesktop.xcb.LibXcb.xcb_grab_keyboard_reply;
import static org.freedesktop.xcb.LibXcb.xcb_ungrab_key;
import static org.freedesktop.xcb.LibXcb.xcb_ungrab_keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_grab_keyboard_cookie_t;
import org.freedesktop.xcb.xcb_grab_mode_t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.input.InputModifier;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;
import org.trinity.foundation.api.display.input.Keyboard;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class XKeyboard implements Keyboard {

	private static final Logger logger = LoggerFactory
			.getLogger(XKeyboard.class);

	private final XKeySymbolMapping xKeySymbolMapping;
	private final XKeySymbolCache xKeySymbolCache;
	private final XInputModifierMaskMapping xInputModifierMaskMapping;
	private final XConnection xConnection;
	private final XTime xTime;
	private final ListeningExecutorService xExecutor;

	@Inject
	XKeyboard(	final XKeySymbolMapping xKeySymbolMapping,
				final XKeySymbolCache xKeySymbolCache,
				final XInputModifierMaskMapping xInputModifierMaskMapping,
				final XConnection xConnection,
				final XTime xTime,
				@Named("Display") final ListeningExecutorService xExecutor) {
		this.xKeySymbolMapping = xKeySymbolMapping;
		this.xKeySymbolCache = xKeySymbolCache;
		this.xInputModifierMaskMapping = xInputModifierMaskMapping;
		this.xConnection = xConnection;
		this.xTime = xTime;
		this.xExecutor = xExecutor;
	}

	private int getWindowId(final DisplaySurface displaySurface) {
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
			XKeyboard.logger.error(XcbErrorUtil.toString(e));
		}
	}

	@Override
	public ListenableFuture<Void>
			grabKey(final DisplaySurface displaySurface,
					final Key catchKey,
					final InputModifiers withModifiers) {

		final int keyCode = catchKey.getKeyCode();
		final int modifiers = withModifiers.getInputModifiersState();
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;

		return this.xExecutor.submit(	new Runnable() {

											@Override
											public void run() {
												xcb_grab_key(	getConnectionRef(),
																(short) 0,
																getWindowId(displaySurface),
																modifiers,
																(short) keyCode,
																(short) pointer_mode,
																(short) keyboard_mode);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void>
			ungrabKey(	final DisplaySurface displaySurface,
						final Key catchKey,
						final InputModifiers withModifiers) {

		final int key = catchKey.getKeyCode();
		final int modifiers = withModifiers.getInputModifiersState();
		final int winId = getWindowId(displaySurface);

		return this.xExecutor.submit(	new Runnable() {
											@Override
											public void run() {
												xcb_ungrab_key(	getConnectionRef(),
																(short) key,
																winId,
																modifiers);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void> ungrabKeyboard() {

		final int time = this.xTime.getTime();

		return this.xExecutor.submit(	new Runnable() {
											@Override
											public void run() {
												xcb_ungrab_keyboard(getConnectionRef(),
																	time);
												xcb_flush(getConnectionRef());
											}
										},
										null);
	}

	@Override
	public ListenableFuture<Void>
			grabKeyboard(final DisplaySurface displaySurface) {
		final int pointer_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int keyboard_mode = xcb_grab_mode_t.XCB_GRAB_MODE_ASYNC;
		final int time = this.xTime.getTime();
		final int winId = getWindowId(displaySurface);

		return this.xExecutor.submit(	new Runnable() {
											@Override
											public void run() {
												final xcb_grab_keyboard_cookie_t grab_keyboard_cookie_t = xcb_grab_keyboard(getConnectionRef(),
																															(short) 0,
																															winId,
																															time,
																															(short) pointer_mode,
																															(short) keyboard_mode);
												final xcb_generic_error_t e = new xcb_generic_error_t();
												// TODO check if grab was
												// successful and
												// return boolean
												xcb_grab_keyboard_reply(getConnectionRef(),
																		grab_keyboard_cookie_t,
																		e);
												checkError(e);
											}
										},
										null);

	}

	@Override
	public ListenableFuture<String>
			asKeySymbolName(final Key key,
							final InputModifiers inputModifiers) {

		final int keyCode = key.getKeyCode();
		final int inputModifiersState = inputModifiers.getInputModifiersState();

		return this.xExecutor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				final Integer keySymbol = XKeyboard.this.xKeySymbolCache
						.getKeySymbol(	keyCode,
										inputModifiersState);

				final String keySymbolName = XKeyboard.this.xKeySymbolMapping
						.toString(keySymbol);
				return keySymbolName;
			}
		});
	}

	@Override
	public ListenableFuture<List<Key>> asKeys(final String keySymbolName) {
		final Integer keySymbol = this.xKeySymbolMapping
				.toKeySymbol(keySymbolName);
		final List<Integer> keyCodes = this.xKeySymbolCache
				.getKeyCodes(keySymbol);

		return this.xExecutor.submit(new Callable<List<Key>>() {
			@Override
			public List<Key> call() throws Exception {
				final List<Key> keys = new ArrayList<Key>(keyCodes.size());
				for (final Integer keyCode : keyCodes) {
					final Key key = new Key(keyCode.intValue());
					keys.add(key);
				}
				return keys;
			}
		});
	}

	@Override
	public InputModifier modifier(final String modifierName) {
		final int mask = this.xInputModifierMaskMapping
				.getXInputModifierMask(modifierName);
		final XInputModifier xInputModifier = new XInputModifier(	mask,
																	modifierName);
		return xInputModifier;
	}

	@Override
	public InputModifiers modifiers(final String... modifierNames) {
		int inputModifiersState = 0;
		for (final String modifierName : modifierNames) {
			inputModifiersState |= this.xInputModifierMaskMapping
					.getXInputModifierMask(modifierName);
		}
		return new InputModifiers(inputModifiersState);
	}
}
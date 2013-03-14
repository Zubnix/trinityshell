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
package org.trinity.shell.input.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.server.input.Keyboard;
import org.trinity.foundation.api.display.shared.event.KeyNotify;
import org.trinity.foundation.api.display.shared.input.KeyboardInput;
import org.trinity.shell.api.input.ShellKeyInputStringBuilder;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

import static com.google.common.util.concurrent.Futures.addCallback;

@Bind
@NotThreadSafe
public class ShellKeyInputStringBuilderImpl implements ShellKeyInputStringBuilder {

	private final String[] ignoreKeys = { Keyboard.BACKSPACE, Keyboard.BEGIN, Keyboard.CAPS_LOCK, Keyboard.CLEAR,
			Keyboard.DELETE, Keyboard.DOWN, Keyboard.END, Keyboard.ENTER, Keyboard.ESCAPE, Keyboard.HOME,
			Keyboard.INSERT, Keyboard.L_ALT, Keyboard.L_CTRL, Keyboard.L_HYPER, Keyboard.L_META, Keyboard.L_SHIFT,
			Keyboard.L_SUPER, Keyboard.LEFT, Keyboard.LINEFEED, Keyboard.NEXT, Keyboard.NUM_LOCK, Keyboard.PAUSE,
			Keyboard.PG_DOWN, Keyboard.PG_UP, Keyboard.PREV, Keyboard.PRINT, Keyboard.R_ALT, Keyboard.R_CTRL,
			Keyboard.R_HYPER, Keyboard.R_META, Keyboard.R_SHIFT, Keyboard.R_SUPER, Keyboard.RIGHT, Keyboard.SCRL_LOCK,
			Keyboard.SHIFT_LOCK, Keyboard.SYS_REQ, Keyboard.TAB, Keyboard.UP };

	interface StringMutatorOnInput {
		void mutate(String keyName);
	}

	private final Keyboard keyboard;
	private StringBuffer stringBuffer = new StringBuffer();
	private final Map<String, StringMutatorOnInput> specialBuildActions;

	/**
	 * 
	 */
	@Inject
	ShellKeyInputStringBuilderImpl(final Keyboard keyboard) {
		this.keyboard = keyboard;
		this.specialBuildActions = new HashMap<String, ShellKeyInputStringBuilderImpl.StringMutatorOnInput>();
		initSpecialBuildActions();
	}

	/**
	 * 
	 */
	protected void initSpecialBuildActions() {
		// first we fill the special build actions with with empty operations
		// for all known special keys & modifiers, that way we make sure nothing
		// fancy happens when a special key or modifier is pressed.
		for (final String specialKeyName : this.ignoreKeys) {
			getSpecialBuildActions().put(	specialKeyName,
											new StringMutatorOnInput() {
												@Override
												public void mutate(final String keyName) {
													// do nothing;
												}
											});
		}

		// next we initialize special keys that do need a specific action

		getSpecialBuildActions().put(	Keyboard.BACKSPACE,
										new StringMutatorOnInput() {
											@Override
											public void mutate(final String keyName) {
												final int length = ShellKeyInputStringBuilderImpl.this.stringBuffer
														.length();
												if (length > 0) {
													// backspace => delete
													// last char
													ShellKeyInputStringBuilderImpl.this.stringBuffer
															.deleteCharAt(length - 1);
												}
											}
										});
	}

	@Override
	public void append(final KeyNotify input) {
		final KeyboardInput keyboardInput = input.getInput();
		final ListenableFuture<String> keyNameFuture = this.keyboard.asKeySymbolName(	keyboardInput.getKey(),
																						keyboardInput
																								.getInputModifiers());

		addCallback(keyNameFuture,
					new FutureCallback<String>() {
						@Override
						public void onSuccess(final String result) {
							final StringMutatorOnInput stringMutatorOnInput = getSpecialBuildActions().get(result);

							if (stringMutatorOnInput != null) {
								stringMutatorOnInput.mutate(result);
							} else {
								// key is not a special key, append it.
								ShellKeyInputStringBuilderImpl.this.stringBuffer.append(result);
							}
						}

						@Override
						public void onFailure(final Throwable t) {
							// TODO Auto-generated method stub

						}
					});

	}

	@Override
	public void clear() {
		this.stringBuffer = new StringBuffer();
	}

	public Map<String, StringMutatorOnInput> getSpecialBuildActions() {
		return this.specialBuildActions;
	}

	@Override
	public void append(final String string) {
		this.stringBuffer.append(string);
	}

	@Override
	public String toString() {
		return this.stringBuffer.toString();
	}
}
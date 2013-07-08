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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.foundation.api.display.event.KeyNotify;
import org.trinity.foundation.api.display.input.Keyboard;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.shell.api.input.ShellKeyInputStringBuilder;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;

// TODO improve threading model for this class
@Bind
@ThreadSafe
public class ShellKeyInputStringBuilderImpl implements ShellKeyInputStringBuilder {

	private final String[] ignoreKeys = { Keyboard.BACKSPACE, Keyboard.BEGIN, Keyboard.CAPS_LOCK, Keyboard.CLEAR,
			Keyboard.DELETE, Keyboard.DOWN, Keyboard.END, Keyboard.ENTER, Keyboard.ESCAPE, Keyboard.HOME,
			Keyboard.INSERT, Keyboard.L_ALT, Keyboard.L_CTRL, Keyboard.L_HYPER, Keyboard.L_META, Keyboard.L_SHIFT,
			Keyboard.L_SUPER, Keyboard.LEFT, Keyboard.LINEFEED, Keyboard.NEXT, Keyboard.NUM_LOCK, Keyboard.PAUSE,
			Keyboard.PG_DOWN, Keyboard.PG_UP, Keyboard.PREV, Keyboard.PRINT, Keyboard.R_ALT, Keyboard.R_CTRL,
			Keyboard.R_HYPER, Keyboard.R_META, Keyboard.R_SHIFT, Keyboard.R_SUPER, Keyboard.RIGHT, Keyboard.SCRL_LOCK,
			Keyboard.SHIFT_LOCK, Keyboard.SYS_REQ, Keyboard.TAB, Keyboard.UP };
	private final Keyboard keyboard;
	private final Map<String, StringMutatorOnInput> specialBuildActions = Collections
			.synchronizedMap(new HashMap<String, ShellKeyInputStringBuilderImpl.StringMutatorOnInput>());
	private StringBuffer stringBuffer = new StringBuffer();

	/**
	 *
	 */
	@Inject
	ShellKeyInputStringBuilderImpl(final Keyboard keyboard) {
		this.keyboard = keyboard;
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
												public void mutate(	final StringBuffer stringBuffer,
																	final String keyName) {
													// do nothing;
												}
											});
		}

		// next we initialize special keys that do need a specific action
		getSpecialBuildActions().put(	Keyboard.BACKSPACE,
										new StringMutatorOnInput() {
											@Override
											public void mutate(	final StringBuffer stringBuffer,
																final String keyName) {
												final int length = stringBuffer.length();
												if (length > 0) {
													// backspace => delete last
													// char
													stringBuffer.deleteCharAt(length - 1);
												}
											}
										});
	}

	@Override
	public synchronized void append(final KeyNotify input) {
		final KeyboardInput keyboardInput = input.getInput();
		final ListenableFuture<String> keyNameFuture = this.keyboard.asKeySymbolName(	keyboardInput.getKey(),
																						keyboardInput
																								.getInputModifiers());
		String result;
		try {
			result = keyNameFuture.get();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		// addCallback(keyNameFuture,
		// new FutureCallback<String>() {
		// @Override
		// public void onSuccess(final String result) {
		final StringMutatorOnInput stringMutatorOnInput = getSpecialBuildActions().get(result);

		if (stringMutatorOnInput != null) {
			stringMutatorOnInput.mutate(ShellKeyInputStringBuilderImpl.this.stringBuffer,
										result);
		} else {
			// key is not a special key, append it.
			ShellKeyInputStringBuilderImpl.this.stringBuffer.append(result);
		}
		// }
		//
		// @Override
		// public void onFailure(final Throwable t) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}

	@Override
	public synchronized void clear() {
		this.stringBuffer = new StringBuffer();
	}

	public Map<String, StringMutatorOnInput> getSpecialBuildActions() {
		return this.specialBuildActions;
	}

	@Override
	public synchronized void append(final String string) {
		this.stringBuffer.append(string);
	}

	@Override
	public synchronized String getString() {
		return this.stringBuffer.toString();
	}

	public interface StringMutatorOnInput {
		void mutate(StringBuffer stringBuffer,
					String keyName);
	}
}

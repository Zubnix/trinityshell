/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.trinity.shell.input.impl;

import java.util.HashMap;
import java.util.Map;

import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.Keyboard;
import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.shell.input.api.KeyInputStringBuilder;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * A <code>KeyInputStringBuilder</code> builds a Java <code>String</code> based
 * on {@link KeyNotifyEvent}s.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class KeyInputStringBuilderImpl implements KeyInputStringBuilder {

	private final String[] ignoreKeys = { Keyboard.BACKSPACE, Keyboard.BEGIN,
			Keyboard.CAPS_LOCK, Keyboard.CLEAR, Keyboard.DELETE, Keyboard.DOWN,
			Keyboard.END, Keyboard.ENTER, Keyboard.ESCAPE, Keyboard.HOME,
			Keyboard.INSERT, Keyboard.L_ALT, Keyboard.L_CTRL, Keyboard.L_HYPER,
			Keyboard.L_META, Keyboard.L_SHIFT, Keyboard.L_SUPER, Keyboard.LEFT,
			Keyboard.LINEFEED, Keyboard.NEXT, Keyboard.NUM_LOCK,
			Keyboard.PAUSE, Keyboard.PG_DOWN, Keyboard.PG_UP, Keyboard.PREV,
			Keyboard.PRINT, Keyboard.R_ALT, Keyboard.R_CTRL, Keyboard.R_HYPER,
			Keyboard.R_META, Keyboard.R_SHIFT, Keyboard.R_SUPER,
			Keyboard.RIGHT, Keyboard.SCRL_LOCK, Keyboard.SHIFT_LOCK,
			Keyboard.SYS_REQ, Keyboard.TAB, Keyboard.UP };

	/**
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
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
	KeyInputStringBuilderImpl(final Keyboard keyboard) {
		this.keyboard = keyboard;
		this.specialBuildActions = new HashMap<String, KeyInputStringBuilderImpl.StringMutatorOnInput>();
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
												final int length = KeyInputStringBuilderImpl.this.stringBuffer
														.length();
												if (length > 0) {
													// backspace => delete
													// last char
													KeyInputStringBuilderImpl.this.stringBuffer
															.deleteCharAt(length - 1);
												}
											}
										});
	}

	/**
	 * @param input
	 */
	@Override
	public void append(final KeyNotifyEvent input) {
		final KeyboardInput keyboardInput = input.getInput();
		final String keyName = this.keyboard.asKeySymbolName(keyboardInput
				.getKey(), keyboardInput.getModifiers());
		final StringMutatorOnInput stringMutatorOnInput = getSpecialBuildActions()
				.get(keyName);

		if (stringMutatorOnInput != null) {
			stringMutatorOnInput.mutate(keyName);
		} else {
			// key is not a special key, append it.
			this.stringBuffer.append(keyName);
		}
	}

	/**
	 * 
	 */
	@Override
	public void clear() {
		this.stringBuffer = new StringBuffer();
	}

	/**
	 * @return
	 */
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

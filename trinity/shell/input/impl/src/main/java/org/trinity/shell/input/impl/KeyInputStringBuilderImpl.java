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

import org.trinity.foundation.input.api.InputModifierName;
import org.trinity.foundation.input.api.SpecialKeyName;
import org.trinity.foundation.input.api.event.KeyNotifyEvent;
import org.trinity.shell.input.api.KeyInputStringBuilder;
import org.trinity.shell.input.api.ManagedKeyboard;

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

	/**
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public static interface StringMutatorOnInput {
		void mutate(StringBuffer stringBuffer, KeyNotifyEvent input);
	}

	private final ManagedKeyboard managedKeyboard;
	private final StringBuffer stringBuffer;
	private final Map<String, StringMutatorOnInput> specialBuildActions;

	/**
	 * 
	 */
	@Inject
	protected KeyInputStringBuilderImpl(final ManagedKeyboard managedKeyboard) {
		this.managedKeyboard = managedKeyboard;
		this.stringBuffer = new StringBuffer();
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
		for (final SpecialKeyName specialKeyName : SpecialKeyName.values()) {
			getSpecialBuildActions().put(	specialKeyName.name(),
											new StringMutatorOnInput() {
												@Override
												public void mutate(	final StringBuffer stringBuffer,
																	final KeyNotifyEvent input) {
													// do nothing;
												}
											});
		}
		for (final InputModifierName inputModifierName : InputModifierName
				.values()) {
			getSpecialBuildActions().put(	inputModifierName.name(),
											new StringMutatorOnInput() {
												@Override
												public void mutate(	final StringBuffer stringBuffer,
																	final KeyNotifyEvent input) {
													// do nothing;
												}
											});
		}

		// next we initialize special keys that do need a specific action
		getSpecialBuildActions().put(	SpecialKeyName.BACKSPACE.name(),
										new StringMutatorOnInput() {
											@Override
											public void mutate(	final StringBuffer stringBuffer,
																final KeyNotifyEvent input) {
												final int length = stringBuffer
														.length();
												if (length > 0) {
													// backspace => delete
													// last char
													stringBuffer
															.deleteCharAt(length - 1);
												}
											}
										});
	}

	/**
	 * @param input
	 */
	@Override
	public void build(final KeyNotifyEvent input) {
		final String keyName = this.managedKeyboard.keyEventToString(input);
		final StringMutatorOnInput stringMutatorOnInput = getSpecialBuildActions()
				.get(keyName);

		if (stringMutatorOnInput != null) {
			stringMutatorOnInput.mutate(getStringBuffer(), input);
		} else {
			// key is not a special key, append it.
			getStringBuffer().append(keyName);
		}
	}

	/**
	 * 
	 */
	@Override
	public void clearBuffer() {
		final int lenght = getStringBuffer().length();
		if (lenght > 0) {
			getStringBuffer().delete(0, lenght);
		}
	}

	/**
	 * @return
	 */
	public Map<String, StringMutatorOnInput> getSpecialBuildActions() {
		return this.specialBuildActions;
	}

	/**
	 * @return
	 */
	@Override
	public StringBuffer getStringBuffer() {
		return this.stringBuffer;
	}
}

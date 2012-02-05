/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */

package org.hyperdrive.input;

import java.util.HashMap;
import java.util.Map;

import org.hydrogen.displayinterface.event.KeyNotifyEvent;
import org.hydrogen.displayinterface.input.Keyboard.ModifierName;
import org.hydrogen.displayinterface.input.Keyboard.SpecialKeyName;
import org.hyperdrive.core.ManagedDisplay;

// TODO documentation
/**
 * A <code>KeyInputStringBuilder</code> builds a Java <code>String</code> based
 * on {@link KeyNotifyEvent}s.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public abstract class KeyInputStringBuilder {

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 * 
	 */
	public static interface StringMutatorOnInput {
		void mutate(StringBuffer stringBuffer, KeyNotifyEvent input);
	}

	private final StringBuffer stringBuffer;

	private final Map<String, StringMutatorOnInput> specialBuildActions;

	/**
	 * 
	 */
	public KeyInputStringBuilder() {
		this.stringBuffer = new StringBuffer();
		this.specialBuildActions = new HashMap<String, KeyInputStringBuilder.StringMutatorOnInput>();
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
			getSpecialBuildActions().put(specialKeyName.name(),
					new StringMutatorOnInput() {
						@Override
						public void mutate(final StringBuffer stringBuffer,
								final KeyNotifyEvent input) {
							// do nothing;
						}
					});
		}
		for (final ModifierName modifierName : ModifierName.values()) {
			getSpecialBuildActions().put(modifierName.name(),
					new StringMutatorOnInput() {
						@Override
						public void mutate(final StringBuffer stringBuffer,
								final KeyNotifyEvent input) {
							// do nothing;
						}
					});
		}

		// next we initialize special keys that do need a specific action
		getSpecialBuildActions().put(SpecialKeyName.BACKSPACE.name(),
				new StringMutatorOnInput() {
					@Override
					public void mutate(final StringBuffer stringBuffer,
							final KeyNotifyEvent input) {
						final int length = stringBuffer.length();
						if (length > 0) {
							// backspace => delete
							// last char
							stringBuffer.deleteCharAt(length - 1);
						}
					}
				});
	}

	/**
	 * 
	 * @param input
	 * 
	 */
	public void build(final KeyNotifyEvent input) {
		final String keyName = getManagedDisplay().getManagedKeyboard()
				.keyEventToString(input);
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
	public void clearBuffer() {
		final int lenght = getStringBuffer().length();
		if (lenght > 0) {
			getStringBuffer().delete(0, lenght);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, StringMutatorOnInput> getSpecialBuildActions() {
		return this.specialBuildActions;
	}

	/**
	 * 
	 * @return
	 */
	public abstract ManagedDisplay getManagedDisplay();

	/**
	 * 
	 * @return
	 */
	public StringBuffer getStringBuffer() {
		return this.stringBuffer;
	}
}

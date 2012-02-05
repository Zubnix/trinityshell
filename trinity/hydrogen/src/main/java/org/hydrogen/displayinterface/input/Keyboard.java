/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.displayinterface.input;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface Keyboard {

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public static enum ModifierName {
		// TODO merge with specialkeyname?

		/**
		 * shift key
		 */
		MOD_SHIFT, MOD_LOCK, MOD_CTRL,
		/**
		 * Alt
		 */
		MOD_1,
		/**
		 * numlock
		 */
		MOD_2, MOD_3,
		/**
		 * Meta key (windows, cmd, ..)
		 */
		MOD_4, MOD_5, MOD_ANY;
	}

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public static enum SpecialKeyName {
		ESCAPE, ENTER, BACKSPACE, TAB, LEFT, RIGHT, UP, DOWN;
	}

	/**
	 * 
	 * @param key
	 * @param inputModifiers
	 * @return
	 * 
	 */
	String keyName(Key key, InputModifiers inputModifiers);

	/**
	 * 
	 * @param keyChar
	 * @return
	 * 
	 */
	Key[] keys(String keyChar);

	/**
	 * 
	 * @param modifierKeyName
	 * @return
	 */
	Modifier modifier(ModifierName modifierKeyName);

	/**
	 * 
	 * @param modifierKeyNames
	 * @return
	 */
	InputModifiers modifiers(ModifierName... modifierKeyNames);
}

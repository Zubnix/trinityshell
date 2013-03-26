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
package org.trinity.shell.api.input;

import java.util.List;

import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Key;

/***************************************
 * Creates key bindings with a specific action. A created key binding is not
 * immediately active, but has to made active explicitly.
 * 
 *************************************** 
 */
public interface KeysBindingFactory {

	/****************************************
	 * Create a new inactive key binding.
	 * 
	 * @param keys
	 *            A {@link List} of {@link Key}s of which at least one should be
	 *            pressed.
	 * @param inputModifiers
	 *            {@link InputModifiers} that should be active.
	 * @param action
	 *            The {@link Runnable} that should be executed when the correct
	 *            keys are pressed.
	 * @return a new {@link ShellKeysBinding}.
	 *************************************** 
	 */
	ShellKeysBinding createKeysBinding(	List<Key> keys,
										InputModifiers inputModifiers,
										KeyBindAction action);
}

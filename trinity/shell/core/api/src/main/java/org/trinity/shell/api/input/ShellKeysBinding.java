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

import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;

/***************************************
 * A specific keybinding. The keybinding will not be bound until a call to
 * {@link ShellKeysBinding#bind()} is made. A bound
 * <code>ShellKeysBinding</code> will be activated as soon as any of the
 * required {@link Key}s is pressed and the required {@link InputModifiers} are
 * active.
 * 
 *************************************** 
 */
public interface ShellKeysBinding {
	/***************************************
	 * The action to perform when this keybinding is executed.
	 * 
	 * @return a {@link Runnable}
	 *************************************** 
	 */
	Runnable getAction();

	/***************************************
	 * A group of possible <code>Keys</code> for this keybinding to become
	 * active.
	 * 
	 * @return {@link Key}s
	 *************************************** 
	 */
	List<Key> getKeys();

	/***************************************
	 * The <code>InputModifiers</code> that are required to be active.
	 * 
	 * @return {@link InputModifiers}
	 *************************************** 
	 */
	InputModifiers getInputModifiers();

	/***************************************
	 * Make this keybinding eligible for execution.
	 *************************************** 
	 */
	void bind();

	/***************************************
	 * Deactive this keybinding.
	 *************************************** 
	 */
	void unbind();
}

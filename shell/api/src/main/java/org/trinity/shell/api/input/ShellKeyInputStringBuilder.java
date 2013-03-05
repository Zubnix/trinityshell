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

import org.trinity.foundation.api.display.event.KeyNotify;

/***************************************
 * Convenience class to build a {@code String} with {@link KeyNotify}s. A call
 * to {@code toString()} reads out the underlying {@code String}.
 * 
 *************************************** 
 */
public interface ShellKeyInputStringBuilder {

	/***************************************
	 * Append the given {@code String} to the underlying {@code String}.
	 * 
	 * @param string
	 *            a {@link String}
	 *************************************** 
	 */
	void append(String string);

	/***************************************
	 * Append the character derived from the given {@code KeyNotify} to the
	 * underlying {@code String}. Backspace will deduct the last character from
	 * the underlying String. Special keys with no clear interpretation, like
	 * alt, ctrl, f1, ... will be ignored.
	 * 
	 * @param input
	 *            a {@link KeyNotify}
	 *************************************** 
	 */
	void append(KeyNotify input);

	/***************************************
	 * Clear the underlying {@code String}.
	 *************************************** 
	 */
	void clear();
}
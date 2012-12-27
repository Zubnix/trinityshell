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

import org.trinity.foundation.api.display.event.KeyNotifyEvent;

/***************************************
 * Convenience class to build a {@link String} with {@link KeyNotifyEvent}s.
 * 
 *************************************** 
 */
public interface ShellKeyInputStringBuilder {

	/***************************************
	 * Append the given {@link String} to the underlying string buffer.
	 * 
	 * @param string
	 *            a {@link String}
	 *************************************** 
	 */
	void append(String string);

	/***************************************
	 * Append the {@link String} derived from the given {@link KeyNotifyEvent}
	 * to the underlying string buffer.
	 * 
	 * @param input
	 *            a {@link KeyNotifyEvent}
	 *************************************** 
	 */
	void append(KeyNotifyEvent input);

	/***************************************
	 * Clear the underlying string buffer.
	 *************************************** 
	 */
	void clear();
}
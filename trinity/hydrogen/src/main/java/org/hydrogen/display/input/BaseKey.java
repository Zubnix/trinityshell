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
package org.hydrogen.display.input;

import org.hydrogen.api.display.input.Key;

// TODO documentation
/**
 * A <code>Key</code> represents a key on a keyboard.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseKey implements Key {

	private final int keyCode;

	/**
	 * The native key code.
	 * 
	 * @param keyCode
	 */
	public BaseKey(final int keyCode) {
		this.keyCode = keyCode;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getKeyCode() {
		return this.keyCode;
	}
}

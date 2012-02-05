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
 * <code>KeyboardInput</code> is user {@link Input} that originated from a
 * keyboard.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class KeyboardInput extends Input {

	private final Key key;
	private final InputModifiers inputModifiers;

	/**
	 * 
	 * @param momentum
	 * @param key
	 * @param inputModifiers
	 */
	public KeyboardInput(final Momentum momentum, final Key key,
			final InputModifiers inputModifiers) {
		super(momentum);
		this.key = key;
		this.inputModifiers = inputModifiers;
	}

	/**
	 * 
	 * @return
	 */
	public Key getKey() {
		return this.key;
	}

	/**
	 * 
	 * @return
	 */
	public InputModifiers getModifiers() {
		return this.inputModifiers;
	}
}

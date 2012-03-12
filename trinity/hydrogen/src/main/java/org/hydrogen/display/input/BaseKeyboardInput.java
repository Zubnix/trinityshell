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

import org.hydrogen.api.display.input.KeyboardInput;
import org.hydrogen.api.display.input.Momentum;

// TODO documentation
/**
 * <code>KeyboardInput</code> is user {@link BaseInput} that originated from a
 * keyboard.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseKeyboardInput extends BaseInput implements KeyboardInput {

	private final BaseKey baseKey;
	private final BaseInputModifiers baseInputModifiers;

	/**
	 * 
	 * @param momentum
	 * @param baseKey
	 * @param baseInputModifiers
	 */
	public BaseKeyboardInput(final Momentum momentum, final BaseKey baseKey,
			final BaseInputModifiers baseInputModifiers) {
		super(momentum);
		this.baseKey = baseKey;
		this.baseInputModifiers = baseInputModifiers;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public BaseKey getKey() {
		return this.baseKey;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public BaseInputModifiers getModifiers() {
		return this.baseInputModifiers;
	}
}

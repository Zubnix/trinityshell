/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.display.api.input.base;

import org.hydrogen.display.api.input.InputModifiers;
import org.hydrogen.display.api.input.Key;
import org.hydrogen.display.api.input.KeyboardInput;
import org.hydrogen.display.api.input.Momentum;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

// TODO documentation
/**
 * <code>KeyboardInput</code> is user {@link BaseInput} that originated from a
 * keyboard.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseKeyboardInput extends BaseInput implements KeyboardInput {

	private final Key baseKey;
	private final InputModifiers baseInputModifiers;

	/**
	 * @param momentum
	 * @param baseKey
	 * @param baseInputModifiers
	 */
	@Inject
	protected BaseKeyboardInput(final Momentum momentum,
								@Assisted final Key key,
								@Assisted final InputModifiers inputModifiers) {
		super(momentum);
		this.baseKey = key;
		this.baseInputModifiers = inputModifiers;
	}

	/**
	 * @return
	 */
	@Override
	public Key getKey() {
		return this.baseKey;
	}

	/**
	 * @return
	 */
	@Override
	public InputModifiers getModifiers() {
		return this.baseInputModifiers;
	}
}

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
package org.trinity.core.input.impl;

import org.trinity.core.input.api.Input;
import org.trinity.core.input.api.Momentum;

import com.google.inject.Inject;

// TODO documentation
/**
 * <code>Input</code> represents user input like a keystroke or a button press.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class InputImpl implements Input {

	private final Momentum momentum;

	/**
	 * 
	 * @param momentum
	 */
	@Inject
	protected InputImpl(final Momentum momentum) {
		this.momentum = momentum;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Momentum getMomentum() {
		return this.momentum;
	}
}

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
package org.trinity.foundation.input.api;

/***************************************
 * Represents any form of user input.
 * 
 *************************************** 
 */
public class Input {

	private Momentum momentum;
	private InputModifiers inputModifiers;

	/***************************************
	 * Construct new <code>Input</code> with the given {@link Momentum}. The
	 * <code>Momentum</code> is used to provide an indication of the state of
	 * the user input.
	 * 
	 * @param momentum
	 *            a {@link Momentum}.
	 *************************************** 
	 */
	public Input(final Momentum momentum, InputModifiers inputModifiers) {
		this.momentum = momentum;
		this.inputModifiers = inputModifiers;
	}

	/***************************************
	 * The state of the user input.
	 * 
	 * @return a {@link Momentum}.
	 *************************************** 
	 */
	public Momentum getMomentum() {
		return this.momentum;
	}

	public InputModifiers getInputModifiers() {
		return inputModifiers;
	}

}

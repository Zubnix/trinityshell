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

import org.hydrogen.api.display.input.Momentum;
import org.hydrogen.api.display.input.MouseInput;

// TODO documentation
/**
 * <code>MouseInput</code> is user {@link BaseInput} that originated from a
 * mouse.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseMouseInput extends BaseInput implements MouseInput {

	private final BaseButton baseButton;
	private final BaseInputModifiers baseInputModifiers;

	private final int rootX;
	private final int rootY;
	private final int relativeX;
	private final int relativeY;

	/**
	 * 
	 * @param momentum
	 * @param baseButton
	 * @param baseInputModifiers
	 * @param rootX
	 * @param rootY
	 * @param relativeX
	 * @param relativeY
	 */
	public BaseMouseInput(final Momentum momentum, final BaseButton baseButton,
			final BaseInputModifiers baseInputModifiers, final int rootX,
			final int rootY, final int relativeX, final int relativeY) {
		super(momentum);
		this.baseButton = baseButton;
		this.baseInputModifiers = baseInputModifiers;
		this.rootX = rootX;
		this.rootY = rootY;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public BaseButton getButton() {
		return this.baseButton;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public BaseInputModifiers getModifiers() {
		return this.baseInputModifiers;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getRelativeX() {
		return this.relativeX;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getRelativeY() {
		return this.relativeY;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getRootX() {
		return this.rootX;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public int getRootY() {
		return this.rootY;
	}
}

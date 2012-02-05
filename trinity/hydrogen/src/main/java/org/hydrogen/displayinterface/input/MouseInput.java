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
 * <code>MouseInput</code> is user {@link Input} that originated from a mouse.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class MouseInput extends Input {

	private final Button button;
	private final InputModifiers inputModifiers;

	private final int rootX;
	private final int rootY;
	private final int relativeX;
	private final int relativeY;

	/**
	 * 
	 * @param momentum
	 * @param button
	 * @param inputModifiers
	 * @param rootX
	 * @param rootY
	 * @param relativeX
	 * @param relativeY
	 */
	public MouseInput(final Momentum momentum, final Button button,
			final InputModifiers inputModifiers, final int rootX,
			final int rootY, final int relativeX, final int relativeY) {
		super(momentum);
		this.button = button;
		this.inputModifiers = inputModifiers;
		this.rootX = rootX;
		this.rootY = rootY;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	/**
	 * 
	 * @return
	 */
	public Button getButton() {
		return this.button;
	}

	/**
	 * 
	 * @return
	 */
	public InputModifiers getModifiers() {
		return this.inputModifiers;
	}

	/**
	 * 
	 * @return
	 */
	public int getRelativeX() {
		return this.relativeX;
	}

	/**
	 * 
	 * @return
	 */
	public int getRelativeY() {
		return this.relativeY;
	}

	/**
	 * 
	 * @return
	 */
	public int getRootX() {
		return this.rootX;
	}

	/**
	 * 
	 * @return
	 */
	public int getRootY() {
		return this.rootY;
	}
}

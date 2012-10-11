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

public class PointerInput extends Input {

	private final Button button;
	private final InputModifiers inputModifiers;
	private final int relativeX;
	private final int relativeY;
	private final int rootX;
	private final int rootY;

	public PointerInput(final Momentum momentum,
						final Button button,
						final InputModifiers inputModifiers,
						final int relativeX,
						final int relativeY,
						final int rootX,
						final int rootY) {
		super(momentum);
		this.button = button;
		this.inputModifiers = inputModifiers;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.rootX = rootX;
		this.rootY = rootY;
	}

	public Button getButton() {
		return this.button;
	}

	public InputModifiers getModifiers() {
		return this.inputModifiers;
	}

	public int getRelativeX() {
		return this.relativeX;
	}

	public int getRelativeY() {
		return this.relativeY;
	}

	public int getRootX() {
		return this.rootX;
	}

	public int getRootY() {
		return this.rootY;
	}
}

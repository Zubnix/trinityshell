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
package org.trinity.foundation.api.display.input;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

import javax.annotation.concurrent.Immutable;

/****************************************
 * User input that came from a pointing device, like a mouse.
 ***************************************
 */
@Immutable
@ExecutionContext(DisplayExecutor.class)
public class PointerInput extends Input {

	private final Button button;
	private final int relativeX;
	private final int relativeY;
	private final int rootX;
	private final int rootY;

	/***************************************
	 * Create a new {@code PointerInput} with the {@link Button} who's
	 * {@link Momentum} has changed.
	 *
	 * @param momentum
	 *            A {@link Momentum}
	 * @param button
	 *            A {@link Button}
	 * @param inputModifiers
	 *            The {@link InputModifiers} that were active when this
	 *            {@code PointerInput} was created.
	 * @param relativeX
	 *            The X position of the pointer relative to the currently
	 *            focused native display area.
	 * @param relativeY
	 *            The Y position of the pointer relative to the currently
	 *            focused native display area.
	 * @param rootX
	 *            The X position of the pointer relative to the root native
	 *            display area.
	 * @param rootY
	 *            The Y position of the pointer relative to the root native
	 *            display area.
	 ***************************************
	 */
	public PointerInput(final Momentum momentum,
						final Button button,
						final InputModifiers inputModifiers,
						final int relativeX,
						final int relativeY,
						final int rootX,
						final int rootY) {
		super(	momentum,
				inputModifiers);
		this.button = button;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.rootX = rootX;
		this.rootY = rootY;
	}

	/***************************************
	 * The {@link Button} who's {@link Momentum} has changed.
	 *
	 * @return a {@link Button}.
	 ***************************************
	 */
	public Button getButton() {
		return this.button;
	}

	/***************************************
	 * The X position of the pointer relative to the currently focused native
	 * display area.
	 *
	 * @return the relative X position, implementation dependent but usually in
	 *         pixels.
	 ***************************************
	 */
	public int getRelativeX() {
		return this.relativeX;
	}

	/***************************************
	 * The Y position of the pointer relative to the currently focused native
	 * display area.
	 *
	 * @return the relative Y position, implementation dependent but usually in
	 *         pixels.
	 ***************************************
	 */
	public int getRelativeY() {
		return this.relativeY;
	}

	/***************************************
	 * The X position of the pointer relative to the root native display area.
	 *
	 * @return the absolute X position, implementation dependent but usually in
	 *         pixels.
	 ***************************************
	 */
	public int getRootX() {
		return this.rootX;
	}

	/***************************************
	 * The Y position of the pointer relative to the root native display area.
	 *
	 * @return the absolute Y position, implementation dependent but usually in
	 *         pixels.
	 ***************************************
	 */
	public int getRootY() {
		return this.rootY;
	}
}

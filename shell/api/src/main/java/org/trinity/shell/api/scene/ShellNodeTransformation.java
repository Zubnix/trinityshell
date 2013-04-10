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
package org.trinity.shell.api.scene;

import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.shared.ImmutableRectangle;
import org.trinity.foundation.api.shared.Rectangle;

/***************************************
 * A geometric transformation. Current geometric property names end in 0, new
 * ones in 1. A Delta value is the subtraction of the new value with the current
 * value.
 *************************************** 
 */
@Immutable
public class ShellNodeTransformation {
	private final Rectangle rect0;
	private final Rectangle rect1;
	private final Rectangle deltaRect;
	private final ShellNodeParent parent0;
	private final ShellNodeParent parent1;

	public ShellNodeTransformation(	final Rectangle rect0,
									final ShellNodeParent parent0,
									final Rectangle rect1,
									final ShellNodeParent parent1) {
		this.rect0 = rect0;
		this.rect1 = rect1;

		this.parent0 = parent0;
		this.parent1 = parent1;

		final int deltaX = rect1.getPosition().getX() - rect0.getPosition().getX();
		final int deltaY = rect1.getPosition().getY() - rect0.getPosition().getY();
		final int deltaWidth = rect1.getSize().getWidth() - rect0.getSize().getWidth();
		final int deltaHeight = rect1.getSize().getWidth() - rect0.getSize().getHeight();
		this.deltaRect = new ImmutableRectangle(deltaX,
												deltaY,
												deltaWidth,
												deltaHeight);
	}

	/***************************************
	 * The current geometry of the node.
	 * 
	 * @return a {@link Rectangle}
	 *************************************** 
	 */
	public Rectangle getRect0() {
		return this.rect0;
	}

	/***************************************
	 * The difference (delta) of the current ({@link #getRect0()}) and the
	 * desired ({@link #getRect1()}) geometry, ie desired width, height, x and y
	 * is subtracted from the current.
	 * 
	 * @return a {@link Rectangle}
	 *************************************** 
	 */
	public Rectangle getDeltaRect() {
		return this.deltaRect;
	}

	/***************************************
	 * The desired geometry of the node.
	 * 
	 * @return a {@link Rectangle}
	 *************************************** 
	 */
	public Rectangle getRect1() {
		return this.rect1;
	}

	/***************************************
	 * The current parent of the node.
	 * 
	 * @return a {@link ShellNodeParent}
	 *************************************** 
	 */
	public ShellNodeParent getParent0() {
		return this.parent0;
	}

	/***************************************
	 * The desired parent of the node.
	 * 
	 * @return a {@link ShellNodeParent}
	 *************************************** 
	 */
	public ShellNodeParent getParent1() {
		return this.parent1;
	}
}

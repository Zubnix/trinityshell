/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.api.scene;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import javax.annotation.concurrent.Immutable;
import javax.media.nativewindow.util.Rectangle;

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
	private final Optional<ShellNodeParent> parent0;
	private final Optional<ShellNodeParent> parent1;

	public ShellNodeTransformation(	final Rectangle rect0,
									final Optional<ShellNodeParent> parent0,
									final Rectangle rect1,
									final Optional<ShellNodeParent> parent1) {
		this.rect0 = rect0;
		this.rect1 = rect1;

		this.parent0 = parent0;
		this.parent1 = parent1;
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
	public Optional<ShellNodeParent> getParent0() {
		return this.parent0;
	}

	/***************************************
	 * The desired parent of the node.
	 *
	 * @return a {@link ShellNodeParent}
	 ***************************************
	 */
	public Optional<ShellNodeParent> getParent1() {
		return this.parent1;
	}

	@Override
	public boolean equals(final Object obj) {
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final ShellNodeTransformation other = (ShellNodeTransformation) obj;

		return Objects.equal(this.rect0,
							 other.rect0)
				&& Objects.equal(this.rect1,
								 other.rect1)
				&& Objects.equal(this.parent0,
								 other.parent0)
				&& Objects.equal(this.parent1,
								 other.parent1);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.rect0,
								this.rect1,
								this.parent0,
								this.parent1);
	}
}

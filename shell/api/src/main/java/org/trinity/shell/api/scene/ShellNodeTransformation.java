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

import com.google.auto.value.AutoValue;
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
@AutoValue
public abstract class ShellNodeTransformation {

	public static ShellNodeTransformation create(final Rectangle rect0,
												 final Rectangle rect1,
												 final Optional<ShellNodeParent> parent0,
												 final Optional<ShellNodeParent> parent1) {
		return new AutoValue_ShellNodeTransformation(rect0,
													 rect1,
													 parent0,
													 parent1);
	}

	/***************************************
	 * The current geometry of the node.
	 *
	 * @return a {@link Rectangle}
	 ***************************************
	 */
	public abstract Rectangle getRect0();

	/***************************************
	 * The desired geometry of the node.
	 *
	 * @return a {@link Rectangle}
	 ***************************************
	 */
	public abstract Rectangle getRect1();

	/***************************************
	 * The current parent of the node.
	 *
	 * @return a {@link ShellNodeParent}
	 ***************************************
	 */
	public abstract Optional<ShellNodeParent> getParent0();

	/***************************************
	 * The desired parent of the node.
	 *
	 * @return a {@link ShellNodeParent}
	 ***************************************
	 */
	public abstract Optional<ShellNodeParent> getParent1();
}

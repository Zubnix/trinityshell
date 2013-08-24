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

import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.bindingkey.ShellExecutor;

/***************************************
 * Executes the actual geometry changes for a {@link ShellNode}. A
 * <code>ShellNodeGeometryDelegate</code> is a delegate for a
 * <code>ShellNode</code> to execute the requested geometry changes.
 * <p>
 * A <code>ShellNodeGeometryDelegate</code> is needed because a tree structure
 * (the shell scene) of different <code>ShellNode</code> subclasses can have
 * non-uniform and deviating on-screen behavior when one of these shell node's
 * geometry changes. A <code>ShellNodeGeometryDelegate</code> implementation is
 * needed to accommodate for this and make sure a change in the geometry of its
 * requester (a node) has the correct and desired on-screen effect.
 ***************************************
 */
@ExecutionContext(ShellExecutor.class)
public interface ShellNodeGeometryDelegate {

	/***************************************
	 * The <code>ShellNode</code> on who's behalf this executer operates.
	 *
	 * @return a {@link ShellNode}
	 ***************************************
	 */
	ShellNode getShellNode();

	void move(Coordinate desiredPosition);

	void resize(Size desiredSize);

	void moveResize(Coordinate desiredPosition,
					Size desiredSize);

	/***************************************
	 * Execute the actual destroy process of the handled <code>ShellNode</code>.
	 ***************************************
	 */
	void destroy();
}

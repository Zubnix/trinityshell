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
package org.trinity.shell.scene.api;

import org.trinity.shell.api.scene.manager.ShellLayoutManager;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

/***************************************
 * A {@link ShellNode} that can have child <code>ShellNode</code>s. A node's
 * parent can be changed by calling {@link ShellNode#setParent(Optional)}
 * followed by either {@link ShellNode#doReparent()} or
 * {@link ShellNode#requestReparent()}. <code>doReparent</code> guarantees that
 * the child will have the desired parent as its new parent.
 * <code>requestReparent</code> delegates the reparenting to any subscribed
 * child node listener, which can be, for example, the current parent's
 * {@link ShellLayoutManager}.
 *
 ***************************************
 */
public interface ShellNodeParent extends ShellNode {

	/***************************************
	 * Layout all child <code>ShellNode</code>s.
	 ***************************************
	 */
	void layout();

	/***************************************
	 * Change the layout manager of this parent to the desired layout manager.
	 *
	 * @param shellLayoutManager
	 *            A {@link ShellLayoutManager}.
	 ***************************************
	 */
	void setLayoutManager(@Nonnull ShellLayoutManager shellLayoutManager);

    void removeLayoutManager();
}

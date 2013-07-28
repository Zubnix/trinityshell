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
package org.trinity.shell.api.scene.manager;

import java.util.List;

import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeParent;

// TODO provide methods to add childs at a specific index.
/***************************************
 * Manages child nodes in a {@link ShellNodeParent}.
 *
 ***************************************
 */
public interface ShellLayoutManager {

	/***************************************
	 * Register a child with no {@link ShellLayoutProperty} so it can be managed
	 * by this layout manger.
	 *
	 * @param child
	 *            A child {@link ShellNode}.
	 ***************************************
	 */
	void addChildNode(ShellNode child);

	/***************************************
	 * Register a child with a specific {@code ShellLayoutProperty} so it can be
	 * managed by this layout manager. The {@code ShellLayoutProperty} will
	 * influence how the child is layed out.
	 *
	 * @param child
	 *            A child {@link ShellNode}
	 * @param layoutProperty
	 *            a {@link ShellLayoutProperty}
	 ***************************************
	 */
	void addChildNode(	final ShellNode child,
						final ShellLayoutProperty layoutProperty);

	/***************************************
	 * The {@code ShellLayoutProperty} that was registered together with the
	 * given child {@link ShellNode}.
	 *
	 * @param child
	 *            A child {@link ShellNode}
	 * @return A {@link ShellLayoutProperty}.
	 ***************************************
	 */
	ShellLayoutProperty getLayoutProperty(final ShellNode child);

	/***************************************
	 * The child {@link ShellNode} at the specified index.
	 *
	 * @param index
	 *            an index. Greater or equal to 0.
	 * @return A child {@link ShellNode}.
	 ***************************************
	 */
	ShellNode getChild(final int index);

	/***************************************
	 * All registered {@link ShellNode}s.
	 *
	 * @return A {@link List} of child {@link ShellNode}s.
	 ***************************************
	 */
	List<ShellNode> getChildren();

	/***************************************
	 * Remove a previously registered child {@link ShellNode}.
	 *
	 * @param child
	 *            A child {@link ShellNode}.
	 ***************************************
	 */
	void removeChild(final ShellNode child);

	/***************************************
	 * Remove the child {@link ShellNode} at the specified index.
	 *
	 * @param index
	 *            an index. Greater or equal to 0.
	 ***************************************
	 */
	void removeChild(final int index);

	/***************************************
	 * Layout all child {@link ShellNode}s. This method should only be called by
	 * a {@link ShellNodeParent}.
	 *
	 * @param containerNode
	 *            The parent {@link ShellNode}.
	 ***************************************
	 */
	void layout(ShellNodeParent parent);
}
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
package org.trinity.shell.api.node.manager;

import java.util.List;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeParent;

// TODO provide methods to add childs at a specific index.
/***************************************
 * Manages child nodes in a {@link ShellNodeParent}.
 * 
 *************************************** 
 */
public interface ShellLayoutManager {

	/***************************************
	 * Register a child so it can be managed by this layout manger.
	 * 
	 * @param child
	 *            A child {@link ShellNode}.
	 *************************************** 
	 */
	void addChildNode(ShellNode child);

	/***************************************
	 * Register a child with a specific property so it can be managed by this
	 * layout manager.
	 * 
	 * @param child
	 * @param layoutProperty
	 *************************************** 
	 */
	void addChildNode(	final ShellNode child,
						final ShellLayoutProperty layoutProperty);

	/***************************************
	 * The layout property that was registered together with the given child
	 * {@link ShellNode}.
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
	 * Layout all child {@link ShellNode}s.
	 * 
	 * @param containerNode
	 *            The parent {@link ShellNode}.
	 *************************************** 
	 */
	void layout(ShellNodeParent parent);
}
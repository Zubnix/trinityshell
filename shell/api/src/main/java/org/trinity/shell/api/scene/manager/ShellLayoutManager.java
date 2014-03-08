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

import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeParent;

import javax.media.nativewindow.util.RectangleImmutable;
import java.util.List;

// TODO provide methods to add childs at a specific index.
/***************************************
 * Manages child nodes in a {@link ShellNodeParent}.
 *
 ***************************************
 */
public interface ShellLayoutManager {

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
	void setChildLayoutConfiguration(final ShellNode child,
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
	ShellLayoutProperty getChildLayoutConfiguration(final ShellNode child);

	/***************************************
	 * Remove a previously registered child {@link ShellNode}.
	 *
	 * @param child
	 *            A child {@link ShellNode}.
	 ***************************************
	 */
	void removeChildLayoutConfiguration(final ShellNode child);

	/***************************************
	 * Layout all child {@link ShellNode}s. This method should only be called by
	 * a {@link ShellNodeParent}.
	 *
	 * @param layoutRegion
     *              The region in which layout should take place
	 * @param childNodes
	 * 				Shell nodes that need to be layed out.
	 ***************************************
	 */
	void layout(RectangleImmutable layoutRegion,
				List<ShellNode> childNodes);
}
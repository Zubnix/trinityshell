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
package org.trinity.shell.api.scene.manager;

import org.trinity.shell.api.scene.ShellNode;

/***************************************
 * Lays out child {@link ShellNode}s in a single continuous line ie in a single
 * row (horizontal), or a single column (vertical). Children can be position
 * vertically, horizontally and left to right or right to left. Each child node
 * can be assigned a weight. Each child weight is then compared to the weight of
 * all children to calculate a child's relative weight. This relative weight is
 * used to calculate the child's change in size. Children with a weight of zero
 * will be interpreted as have a static size.
 *************************************** 
 */
public interface ShellLayoutManagerLine extends ShellLayoutManager {

	/***************************************
	 * Set this layout manager orientation.
	 * 
	 * @param horizontalDirection
	 *            True for horizontal, false for vertical ortientation.
	 *************************************** 
	 */
	void setHorizontalDirection(boolean horizontalDirection);

	/***************************************
	 * Set this layout manager direction.
	 * 
	 * @param inverseDirection
	 *            True for right to left, false for left to right direction.
	 *************************************** 
	 */
	void setInverseDirection(boolean inverseDirection);

	@Override
	public ShellLayoutPropertyLine getLayoutProperty(ShellNode child);

}

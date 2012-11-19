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
package org.trinity.foundation.render.api;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.shared.geometry.api.Rectangle;

/****************************************
 * A node to implement a tree of <code>DisplayArea</code>s. It provides
 * functions to retrieve information about its geometry in this tree. It offers
 * no ways of manipulating its visual state directly.
 * <p>
 * Each <code>SurfaceNode</code> has a parent <code>SurfaceNode</code> . If a
 * <code>SurfaceNode</code> is repositioned, all it's children will be
 * repositioned by the same offset.
 * <p>
 * A <code>SurfaceNode</code> has an absolute X and a Y coordinate, based on the
 * <code>SurfaceNode</code>'s topmost parent, the 'root' parent.
 * <p>
 * A <code>SurfaceNode</code> has a relative X and Y coordinate. This coordinate
 * is based on the <code>SurfaceNode</code>'s position relative to its direct
 * parent.
 * <p>
 * A <code>SurfaceNode</code> has a positive, non-zero width and a height.
 * <p>
 * A <code>SurfaceNode</code> has a visibility of visible or invisible.
 * 
 *************************************** 
 */
public interface SurfaceNode extends DisplayArea, Rectangle {

	/***************************************
	 * The absolute X value of this <code>SurfaceNode</code>.
	 * <p>
	 * The absolute value is the distance, implementation dependent but usually
	 * in pixels, to root parent's position.
	 * 
	 * @return An absolute X coordinate.
	 *************************************** 
	 */
	int getAbsoluteX();

	/***************************************
	 * The absolute Y value of this <code>SurfaceNode</code>.
	 * <p>
	 * The absolute value is the distance, implementation dependent but usually
	 * in pixels, to root parent's position.
	 * 
	 * @return An absolute Y coordinate.
	 *************************************** 
	 */
	int getAbsoluteY();

	/**
	 * Indicates if this <code>SurfaceNode</code> is visible. This is
	 * implementation dependent. A <code>SurfaceNode</code> is usually only
	 * visible if it's parent is visible. So even though this method may return
	 * true, the <code>SurfaceNode</code> will only be physically visible if all
	 * it's parents are physically visible as well.
	 * 
	 * @return
	 */
	boolean isVisible();

	/**
	 * The direct parent.
	 * 
	 * @return A SurfaceNode.
	 */
	SurfaceNode getParent();
}

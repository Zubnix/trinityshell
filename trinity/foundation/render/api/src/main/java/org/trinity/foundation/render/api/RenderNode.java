/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.render.api;

import org.trinity.foundation.display.api.DisplayArea;
import org.trinity.foundation.shared.geometry.api.Rectangle;

// TODO documentation
/**
 * A <code>RenderNode</code> provide a mechanism to implement a tree structure
 * of <code>DisplayArea</code>s. It provides functions to retrieve information
 * about its geometry in the tree. It offers no ways of manipulating its visual
 * state. It represents a node in a tree structure of <code>RenderNode</code>s.
 * A <code>RenderNode</code> lives in an inverted Y-axis orthogonal space. This
 * space has its center positioned in the top left corner of the user's screen.
 * This means that a higher Y value indicates a lower geographical position on
 * the user's screen. A higher X value means a positioned more to the right on
 * the user's screen.
 * <p>
 * Each <code>RenderNode</code> has a parent <code>RenderNode</code> . If a
 * <code>RenderNode</code> is repositioned, all it's children will be
 * repositioned by the same offset.
 * <p>
 * A <code>RenderNode</code> has an absolute X and a Y coordinate, based on the
 * <code>RenderNode</code>'s top left corner relative to the center of the
 * Y-axis inverted orthogonal space.
 * <p>
 * A <code>RenderNode</code> has a relative X and Y coordinate. This coordinate
 * is based on the <code>RenderNode</code>'s top left corner relative to its
 * parent top left corner.
 * <p>
 * A <code>RenderNode</code> has a positive, non-zero width and a height.
 * <p>
 * A <code>RenderNode</code> has a visibility of visible or invisible.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface RenderNode extends DisplayArea, Rectangle {
	/**
	 * The absolute X value of this <code>HierarchicalArea</code>.
	 * <p>
	 * An absolute value is the distance in pixels to the center of the inverted
	 * Y-axis orthogonal space.
	 * 
	 * @return An absolute X coordinate.
	 */
	int getAbsoluteX();

	/**
	 * The absolute Y value of this <code>HierarchicalArea</code>.
	 * <p>
	 * An absolute value is the distance in pixels to the center of the inverted
	 * Y-axis orthogonal space.
	 * 
	 * @return An absolute Y coordinate.
	 */
	int getAbsoluteY();

	/**
	 * @return
	 */
	boolean isVisible();

	/**
	 * @return
	 */
	RenderNode getParent();
}

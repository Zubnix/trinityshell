/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.api.display;

import org.hydrogen.api.geometry.Coordinates;

/**
 * An <code>AreaManipulator</code> provides the operations that are available
 * for manipulating and interacting with an {@link Area}. An
 * <code>AreaManipulator</code> instance is bound to exactly one
 * <code>Area</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public interface AreaManipulator<AREATYPE extends Area> {

	/**
	 * Destroy the <code>Area</code>. A destroyed <code>Area</code> is no longer
	 * a candidate for any manipulation.
	 * 
	 */
	void destroy();

	/**
	 * Give an <code>Area</code> input focus. All input is reported with respect
	 * to the <code>Area</code> that has the input focus.
	 * 
	 */
	void setInputFocus();

	/**
	 * Move the <code>Area</code> to the bottom of the stack. This will draw the
	 * <code>Area</code> is it were behind all other <code>Area</code>s.
	 * 
	 * 
	 */
	void lower();

	/**
	 * Make the <code>Area</code> visible if was previously invisible. This does
	 * not necessarily mean that the <code>Area</code> will be visible on-screen
	 * as it can still be hidden behind another <code>Area</code>.
	 * 
	 * 
	 */
	void show();

	/**
	 * Move the <code>Area</code> relative to its parent position.
	 * 
	 * @param x
	 *            The new X coordinate in pixels relative to the parent X
	 *            coordinate.
	 * @param y
	 *            The new Y coordinate in pixels relative to the parent X
	 *            coordinate.
	 * 
	 */
	void move(int x, int y);

	/**
	 * Move and resize the <code>Area</code>. Moving the <code>Area</code> is
	 * done relative to the parent <code>Area</code>.
	 * 
	 * @param x
	 *            The new X coordinate in pixels relative to the parent X
	 *            coordinate.
	 * @param y
	 *            The new Y coordinate in pixels relative to the parent X
	 *            coordinate.
	 * @param width
	 *            The new with in pixels.
	 * @param height
	 *            The new height in pixels.
	 * 
	 */
	void moveResize(int x, int y, int width, int height);

	/**
	 * Move the <code>Area</code> to the top of the stack. This will draw the
	 * <code>Area</code> as if it were above all other <code>Area</code>s.
	 * 
	 */
	void raise();

	/**
	 * Change the parent of the <code>Area</code> with the given X and Y
	 * coordinates as the new position relative to the new parent.
	 * 
	 * @param parent
	 *            The new parent.
	 * @param x
	 *            The new X coordinate in pixels.
	 * @param y
	 *            The new Y coordinate in pixels.
	 */
	void setParent(AREATYPE parent, int x, int y);

	/**
	 * Change the size of the <code>Area</code> to the given width and height.
	 * 
	 * @param width
	 *            The new width in pixels.
	 * @param height
	 *            The new height in pixels.
	 */
	void resize(int width, int height);

	/**
	 * Make the <code>Area</code> invisible.
	 * 
	 */
	void hide();

	Coordinates translateCoordinates(AREATYPE source, int sourceX, int sourceY);
}

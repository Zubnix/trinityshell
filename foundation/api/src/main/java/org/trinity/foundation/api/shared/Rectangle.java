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
package org.trinity.foundation.api.shared;

/***************************************
 * A 2 dimensional rectangle with a position and a dimension.
 * 
 *************************************** 
 */
public interface Rectangle {

	/**
	 * Horizontal position of the rectangle, measured from the top left corner.
	 * 
	 * @return an X position.
	 */
	int getX();

	/**
	 * Vertical position of the rectangle, measure from the top left corner.
	 * 
	 * @return a Y position.
	 */
	int getY();

	/**
	 * Horizontal size of the rectangle.
	 * 
	 * @return A width.
	 */
	int getWidth();

	/**
	 * Vertical size of the rectangle.
	 * 
	 * @return a height.
	 */
	int getHeight();
}

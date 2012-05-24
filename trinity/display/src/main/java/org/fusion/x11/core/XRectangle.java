/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core;

//currently unused
//TODO documentation
//TODO instead of defining a seperate blingbling class hierarchy we might want to implement a 
//set of interfaces defining composite+shape+render functionality. 
//TODO use a (new) rectangle interface defined in hydrogen
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XRectangle {

	private final int x, y, width, height;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public XRectangle(final int x, final int y, final int width,
			final int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/**
	 * 
	 * @return
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * 
	 * @return
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * 
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * 
	 * @return
	 */
	public int getHeight() {
		return this.height;
	}
}

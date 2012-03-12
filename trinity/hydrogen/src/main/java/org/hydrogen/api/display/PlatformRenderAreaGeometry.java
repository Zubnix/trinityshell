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

//TODO documentation
/**
 * A <code>PlatformRenderAreaGeometry</code> contains the native geometric
 * information from a <code>PlatformRenderArea</code> at the time of it's
 * instantiation.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface PlatformRenderAreaGeometry {

	/**
	 * The height in pixels of the <code>PlatformRenderArea</code>.
	 * 
	 * @return The height of the <code>PlatformRenderArea</code>.
	 */
	int getHeight();

	/**
	 * The width in pixels of the <code>PlatformRenderArea</code>.
	 * 
	 * @return The width of the <code>PlatformRenderArea</code>.
	 */
	int getWidth();

	/**
	 * The X coordinate of the <code>PlatformRenderArea</code> in pixels.
	 * Starting from the top left corner of the <code>PlatformRenderArea</code>
	 * 's parent.
	 * 
	 * @return The horizontal coordinate the <code>PlatformRenderArea</code>.
	 */
	int getRelativeX();

	/**
	 * The Y coordinate of the <code>PlatformRenderArea</code> in pixels.
	 * Starting from the top left corner of the <code>PlatformRenderArea</code>
	 * 's parent.
	 * 
	 * @return The vertical coordinate the <code>PlatformRenderArea</code>.
	 */
	int getRelativeY();
}

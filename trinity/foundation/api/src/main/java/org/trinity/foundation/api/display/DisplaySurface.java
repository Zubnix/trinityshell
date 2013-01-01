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
package org.trinity.foundation.api.display;

import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.Rectangle;

/**
 * Represents a native isolated graphical area. Usually a
 * <code>DisplaySurface</code> maps to a native window from a native display.
 * 
 */
public interface DisplaySurface extends DisplayArea, DisplayAreaManipulator {

	/***************************************
	 * Query geometry information. The values of the returned {@link Rectangle}
	 * are implementation dependent but are usually in pixels.
	 * 
	 * @return a {@link Rectangle} corresponding to this {@link DisplaySurface}
	 *         's parent relative position, a with and a hight.
	 *************************************** 
	 */
	Rectangle getGeometry();

	/***************************************
	 * The position of the pointer as seen from this <code>DisplaySurface</code>
	 * 's coordinate system.
	 * 
	 * @return The pointer position {@link Coordinate}.
	 *************************************** 
	 */
	Coordinate getPointerCoordinate();

	/***************************************
	 * Return the handle of the underlying native resource.
	 * 
	 * @return a {@link DisplaySurfaceHandle}
	 *************************************** 
	 */
	DisplaySurfaceHandle getDisplaySurfaceHandle();
}

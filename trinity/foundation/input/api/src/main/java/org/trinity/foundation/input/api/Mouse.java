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
package org.trinity.foundation.input.api;

import org.trinity.foundation.shared.geometry.api.Coordinates;

/**
 * A <code>Mouse</code> is a graphical pointer representing a native pointing
 * device. It's main usage is to provide location based user input.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface Mouse {

	// /**
	// * Get the the X coordinate of this <code>Mouse</code> inside the root
	// * <code>PlatformRenderArea</code> at the time of the last
	// * {@link Mouse#refreshInfo()} call.
	// * <p>
	// * A coordinate is always measured from the top left corner of the root
	// * <code>PlatformRenderArea</code>.
	// *
	// * @return the horizontal value in pixels of this <code>Mouse</code> 's
	// * location.
	// */
	// int getRootX();
	//
	// /**
	// * Get the the Y coordinate of this <code>Mouse</code> inside the root
	// * <code>PlatformRenderArea</code> at the time of the last
	// * {@link Mouse#refreshInfo()} call.
	// * <p>
	// * A coordinate is always measured from the top left corner of the root
	// * <code>PlatformRenderArea</code>.
	// *
	// * @return the vertical value in pixels of this <code>Mouse</code> 's
	// * location.
	// */
	// int getRootY();

	Coordinates getRootCoordinates();

	// /**
	// * Renews the values returned by the other methods in this
	// * <code>Mouse</code> to match the native mouse pointer.
	// */
	// void refreshInfo();
}

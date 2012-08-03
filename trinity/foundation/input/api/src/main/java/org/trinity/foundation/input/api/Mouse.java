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

import java.awt.Button;

import org.trinity.foundation.display.api.DisplayRenderArea;
import org.trinity.foundation.shared.geometry.api.Coordinate;

/**
 * A <code>Mouse</code> is a graphical pointer representing a native pointing
 * device. It's main usage is to provide location based user input.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface Mouse {

	Coordinate getRootCoordinates();

	/**
	 * @param likeButton
	 * @param withModifiers
	 */
	void catchMouseInput(	DisplayRenderArea displayRenderArea,
							Button catchButton,
							InputModifiers withModifiers);

	/**
	 * 
	 */
	void catchAllMouseInput();

	/**
	 * 
	 */
	void stopMouseInputCatching();

	/**
	 * @param likeButton
	 * @param withModifiers
	 */
	void disableMouseInputCatching(	Button likeButton,
									InputModifiers withModifiers);
}

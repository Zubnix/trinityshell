/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.api.display;

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.shared.Listenable;
import org.trinity.foundation.api.shared.Rectangle;


/**
 * Represents a native isolated graphical area. A {@code DisplaySurface}
 * maps to a native window from the native display system.
 *
 */
@ThreadSafe
public interface DisplaySurface extends Listenable {

	/***************************************
	 * Destroy this DisplaySurface. A destroyed DisplaySurface
	 * should be disposed and should not accept any calls.
	 ***************************************
	 */
	void destroy();

	/***************************************
	 * Set the input focus to this DisplaySurface. Generated input
	 * evens will have their source set as coming from this DisplaySurface.
	 * <p>
	 * The effects of giving focus to a hidden or destroyed DisplaySurface
	 * is implementation dependent.
	 ***************************************
	 */
    @Deprecated
	void setInputFocus();

	/***************************************
	 * Move this DisplaySurface to the given coordinates.
	 *
	 * @param x
	 *            The X coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 * @param y
	 *            The Y coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 ***************************************
	 */
	void move(int x,
	                            int y);

	/***************************************
	 * Perform a move and resize operation on this DisplaySurface.
	 *
	 *
	 * @param x
	 *            The X coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 * @param y
	 *            The Y coordinate. Usually in pixels but can be implementation
	 *            dependent.
	 * @param width
	 *            The width. Usually in pixels but can be implementation
	 *            dependent.
	 * @param height
	 *            The height. Usually in pixels but can be implementation
	 *            dependent.
	 * @see #move(int, int)
	 * @see #resize(int, int)
	 *
	 ***************************************
	 */
	void moveResize(	int x,
	                                      int y,
	                                      int width,
	                                      int height);

	/***************************************
	 * Set the size of this DisplaySurface.
	 *
	 * @param width
	 *            The width. Usually in pixels but can be implementation
	 *            dependent.
	 * @param height
	 *            The width. Usually in pixels but can be implementation
	 *            dependent.
	 ***************************************
	 */
	void resize(	int width,
	                                  int height);

	/***************************************
	 * Query geometry information. The values of the returned {@link Rectangle}
	 * are display server dependent but are usually in pixels.
	 *
	 * @return a future {@link Rectangle} corresponding to this
	 *          position, width and a height.
	 ***************************************
	 */
	Rectangle getGeometry();

	/***************************************
	 * Return the handle of the underlying native resource.
	 *
	 * @return a {@link DisplaySurfaceHandle}
	 ***************************************
	 */
	DisplaySurfaceHandle getDisplaySurfaceHandle();


	@Deprecated
	void show();

	@Deprecated
	void hide();

	@Deprecated
	void raise();
}

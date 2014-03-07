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

import org.trinity.foundation.api.shared.Listenable;

import javax.annotation.concurrent.ThreadSafe;
import javax.media.nativewindow.util.Rectangle;
import javax.media.nativewindow.util.RectangleImmutable;


/**
 * Represents a native isolated graphical area. A {@code DisplaySurface}
 * maps to a native window from the native display system.
 */
@ThreadSafe
public interface DisplaySurface extends Listenable {

	/**
	 * ************************************
	 * Destroy this DisplaySurface. A destroyed DisplaySurface
	 * should be disposed and should not accept any calls.
	 * **************************************
	 */
	void destroy();

	/**
	 * ************************************
	 * Set the size of this DisplaySurface.
	 *
	 * @param width  The width. Usually in pixels but can be implementation
	 *               dependent.
	 * @param height The width. Usually in pixels but can be implementation
	 *               dependent.
	 *               **************************************
	 */
	void configure(int x,
				   int y,
				   int width,
				   int height);

	/**
	 * ************************************
	 * Query geometry information. The values of the returned {@link Rectangle}
	 * are display server dependent but are usually in pixels.
	 *
	 * @return a future {@link Rectangle} corresponding to this
	 * position, width and a height.
	 * **************************************
	 */
	RectangleImmutable getShape();

	RectangleImmutable getInputRegion();

	/**
	 * ************************************
	 * Return the handle of the underlying native resource.
	 *
	 * @return a {@link DisplaySurfaceHandle}
	 * **************************************
	 */
	DisplaySurfaceHandle getDisplaySurfaceHandle();
}

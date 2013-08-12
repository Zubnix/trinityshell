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

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Rectangle;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Represents a native isolated graphical area. A <code>DisplaySurface</code>
 * maps to a native window from the native display system.
 *
 */
@ExecutionContext(DisplayExecutor.class)
@ThreadSafe
public interface DisplaySurface extends DisplayArea, DisplayAreaManipulator, AsyncListenable {

	/***************************************
	 * Query geometry information. The values of the returned {@link Rectangle}
	 * are display server dependent but are usually in pixels.
	 *
	 * @return a future {@link Rectangle} corresponding to this
	 *         {@link DisplaySurface} position, width and a height.
	 ***************************************
	 */
	ListenableFuture<Rectangle> getGeometry();

	/***************************************
	 * Return the handle of the underlying native resource.
	 *
	 * @return a {@link DisplaySurfaceHandle}
	 ***************************************
	 */
	DisplaySurfaceHandle getDisplaySurfaceHandle();

}

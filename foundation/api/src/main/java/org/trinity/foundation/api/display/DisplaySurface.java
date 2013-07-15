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

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Rectangle;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Represents a native isolated graphical area. Usually a
 * <code>DisplaySurface</code> maps to a native window from a native display
 * server.
 *
 */
@ExecutionContext(DisplayExecutor.class)
public interface DisplaySurface extends DisplayArea, DisplayAreaManipulator, AsyncListenable {

	/***************************************
	 * Query geometry information. The values of the returned {@link Rectangle}
	 * are display server dependent but are usually in pixels (X11).
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

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
import org.trinity.foundation.api.shared.ExecutionContext;

import javax.annotation.concurrent.NotThreadSafe;

/***************************************
 * Creates {@link DisplaySurface}s based on their {@link DisplaySurfaceHandle}.
 * Ideally multiple calls to {@link #createDisplaySurface(DisplaySurfaceHandle)}
 * with the same {@link DisplaySurfaceHandle} argument should return the same
 * {@link DisplaySurface} instance.
 *
 ***************************************
 */
@ExecutionContext(DisplayExecutor.class)
@NotThreadSafe
public interface DisplaySurfaceFactory {

	// FIXME this should also return a future. the display surface creation
	// should be done in the Display thread.
	/***************************************
	 * Create a new {@link DisplaySurface} with the provided
	 * {@link DisplaySurfaceHandle} as reference to the underlying native
	 * resource.
	 *
	 * @param displaySurfaceHandle
	 *            a {@link DisplaySurfaceHandle}
	 * @return a {@link DisplaySurface}.
	 ***************************************
	 */
	DisplaySurface createDisplaySurface(DisplaySurfaceHandle displaySurfaceHandle);
}

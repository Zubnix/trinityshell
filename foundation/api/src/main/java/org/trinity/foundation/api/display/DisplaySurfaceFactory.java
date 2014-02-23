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

import javax.annotation.concurrent.NotThreadSafe;

/***************************************
 * Creates {@link DisplaySurface}s based on their {@link DisplaySurfaceHandle}.
 * Multiple calls to {@link #create(DisplaySurfaceHandle)}
 * with the same {@link DisplaySurfaceHandle} arguments do not necessarily return the same
 * {@link DisplaySurface} instances.
 *
 ***************************************
 */
@NotThreadSafe
public interface DisplaySurfaceFactory {

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
	DisplaySurface create(DisplaySurfaceHandle displaySurfaceHandle);
}

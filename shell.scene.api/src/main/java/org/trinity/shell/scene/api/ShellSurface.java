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
package org.trinity.shell.scene.api;

import javax.media.nativewindow.util.Dimension;
import javax.media.nativewindow.util.DimensionImmutable;

/***************************************
 * Wraps a {@link DisplaySurface} and provides additional basic functionality
 * like state information, minimum, maximum, current and requested dimensions.
 * {@code ShellSurface} extends from {@link ShellNode} so it can be used within
 * the shell scene graph.
 * <p>
 * Any {@link DisplayEvent} that relates to the underlying
 * {@link DisplaySurface}, is translated to the related {@link ShellNodeEvent}
 * and emitted by this shell surface.
 *
 ***************************************
 */
public interface ShellSurface extends ShellNode {

	/***************************************
	 * The maximum height and width.
	 *
	 * @return a future {@link Dimension}.
	 * @see #setMaxSize(Dimension)
	 ***************************************
	 */
	DimensionImmutable getMaxSize();

	/***************************************
	 * The minimum height and width.
	 *
	 * @return a future {@link Dimension}
	 * @see #setMinSize(Dimension)
	 ***************************************
	 */
	DimensionImmutable getMinSize();

	/***************************************
	 * The underlying, {@code DisplaySurface} that this shell surface will use
	 * to display it's contents. A display surface can be 'shared', so it is
	 * possible that multiple shell surface's use the same display surface.
	 *
	 * @return a platform display buffer.
	 ***************************************
	 */
	Object getDisplayBuffer();

}

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
package org.trinity.shell.api.surface;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.event.ShellNodeEvent;

import com.google.common.util.concurrent.ListenableFuture;

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
	 * The desired delta of the old and new height when this surface's height
	 * changes.
	 * 
	 * @return A future height delta, implementation dependent but usually in
	 *         pixels.
	 * @see #setHeightIncrement(int)
	 *************************************** 
	 */
	ListenableFuture<Integer> getHeightIncrement();

	/***************************************
	 * 
	 * @return
	 * @see #setMaxSize(Size)
	 *************************************** 
	 */
	ListenableFuture<Size> getMaxSize();

	/***************************************
	 * 
	 * @return
	 * @see #setMinSize(Size)
	 *************************************** 
	 */
	ListenableFuture<Size> getMinSize();

	/***************************************
	 * The underlying, {@code DisplaySurface} that this shell surface will use
	 * to display it's contents. A display surface can be 'shared', so it is
	 * possible that multiple shell surface's use the same display surface.
	 * 
	 * @return a future {@link DisplaySurface}.
	 *************************************** 
	 */
	ListenableFuture<DisplaySurface> getDisplaySurface();

	/***************************************
	 * The desired delta of the old and new width when this surface's width
	 * changes.
	 * 
	 * @return A future height delta, implementation dependent but usually in
	 *         pixels.
	 * @see #setWidthIncrement(int)
	 *************************************** 
	 */
	ListenableFuture<Integer> getWidthIncrement();

	/***************************************
	 * Indicates if this surface can be moved.
	 * 
	 * @return True if this surface can be moved, false if not.
	 *************************************** 
	 */
	ListenableFuture<Boolean> isMovable();

	/***************************************
	 * Indicates if this surface can be resized.
	 * 
	 * @return True if this surface can be resized, false if not.
	 *************************************** 
	 */
	ListenableFuture<Boolean> isResizable();

	/***************************************
	 * Set the height delta when the height is changed.
	 * 
	 * @param heightIncrement
	 *            A height delta, implementation dependent but usually in
	 *            pixels.
	 * @see #getHeightIncrement()
	 *************************************** 
	 */
	ListenableFuture<Void> setHeightIncrement(final int heightIncrement);

	/***************************************
	 * 
	 * @param size
	 * @return
	 * @see #getMaxSize()
	 *************************************** 
	 */
	ListenableFuture<Void> setMaxSize(final Size size);

	/***************************************
	 * 
	 * @param size
	 * @return
	 * @see #getMinSize()
	 *************************************** 
	 */
	ListenableFuture<Void> setMinSize(final Size size);

	/***************************************
	 * Indicate if this surface is movable.
	 * 
	 * @param movable
	 *            True if this surface should be movable, false if not.
	 * @see #isMovable()
	 *************************************** 
	 */
	ListenableFuture<Void> setMovable(final boolean movable);

	/***************************************
	 * Indicate if this surface is resizable.
	 * 
	 * @param isResizable
	 *            True if this surface should be resizable, false if not.
	 * @see #isResizable()
	 *************************************** 
	 */
	ListenableFuture<Void> setResizable(final boolean isResizable);

	/***************************************
	 * Set the width delta when the width is changed.
	 * 
	 * @param widthIncrement
	 *            A width delta, implementation dependent but usually in pixels.
	 * @see #getWidthIncrement()
	 *************************************** 
	 */
	ListenableFuture<Void> setWidthIncrement(final int widthIncrement);

	/***************************************
	 * Make this surface's shell geometry match the on-screen geometry of the
	 * backing {@link DisplaySurface}.
	 * 
	 * @see #getDisplaySurface()
	 *************************************** 
	 */
	ListenableFuture<Void> syncGeoToDisplaySurface();
}

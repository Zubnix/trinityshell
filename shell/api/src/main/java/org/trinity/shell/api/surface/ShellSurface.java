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
package org.trinity.shell.api.surface;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.Size;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.event.ShellNodeEvent;

import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

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
public interface ShellSurface extends ShellNodeParent {

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
	 * The maximum height and width.
	 *
	 * @return a future {@link Size}.
	 * @see #setMaxSize(Size)
	 ***************************************
	 */
	ListenableFuture<Size> getMaxSize();

	/***************************************
	 * The minimum height and width.
	 *
	 * @return a future {@link Size}
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
	DisplaySurface getDisplaySurface();

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
	 * @return a future True if this surface can be moved, a future false if
	 *         not.
	 ***************************************
	 */
	ListenableFuture<Boolean> isMovable();

	/***************************************
	 * Indicates if this surface can be resized.
	 *
	 * @return a future True if this surface can be resized, a future false if
	 *         not.
	 ***************************************
	 */
	ListenableFuture<Boolean> isResizable();

	/***************************************
	 * Set the height delta when the height is changed.
	 *
	 * @param heightIncrement
	 *            A height delta, implementation dependent but usually in
	 *            pixels.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #getHeightIncrement()
	 ***************************************
	 */
	ListenableFuture<Void> setHeightIncrement(@Nonnegative final int heightIncrement);

	/***************************************
	 * Change the maximum size. Attempts to change to size of this node beyond
	 * the maximum size are silently ignored.
	 *
	 * @param size
	 *            a {@link Size}
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #getMaxSize()
	 ***************************************
	 */
	ListenableFuture<Void> setMaxSize(@Nonnull final Size size);

	/***************************************
	 *
	 * @param size
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #getMinSize()
	 ***************************************
	 */
	ListenableFuture<Void> setMinSize(@Nonnull final Size size);

	/***************************************
	 * Indicate if this surface is movable.
	 *
	 * @param movable
	 *            True if this surface should be movable, false if not.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #isMovable()
	 ***************************************
	 */
	ListenableFuture<Void> setMovable(final boolean movable);

	/***************************************
	 * Indicate if this surface is resizable.
	 *
	 * @param isResizable
	 *            True if this surface should be resizable, false if not.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #isResizable()
	 ***************************************
	 */
	ListenableFuture<Void> setResizable(final boolean isResizable);

	/***************************************
	 * Set the width delta when the width is changed.
	 *
	 * @param widthIncrement
	 *            A width delta, implementation dependent but usually in pixels.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #getWidthIncrement()
	 ***************************************
	 */
	ListenableFuture<Void> setWidthIncrement(@Nonnegative final int widthIncrement);
}

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
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.event.ShellNodeEvent;

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
	 * The constant absolute delta of the old and new height when this surface's
	 * height changes.
	 * 
	 * @return A height delta, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	int getHeightIncrement();

	/***************************************
	 * The maximum height of this surface.
	 * 
	 * @return a height, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	int getMaxHeight();

	/***************************************
	 * The maximum width of this surface.
	 * 
	 * @return a width, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	int getMaxWidth();

	/***************************************
	 * The minimum height of this surface.
	 * 
	 * @return a height, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	int getMinHeight();

	/***************************************
	 * The minimum width of this surface.
	 * 
	 * @return a width, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	int getMinWidth();

	/***************************************
	 * The underlying, {@code DisplaySurface} that this shell surface will use
	 * to display it's contents. A display surface can be shared, so multiple
	 * shell surface's can use the same display surface.
	 * 
	 * @return an optional {@link DisplaySurface}. If the {@code DisplaySurface}
	 *         is absent, then this node is not yet initialized.
	 *************************************** 
	 */
	DisplaySurface getDisplaySurface();

	/***************************************
	 * The constant absolute delta of the old and new width when this surface's
	 * width changes.
	 * 
	 * @return A height delta, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	int getWidthIncrement();

	/***************************************
	 * Indicates if this surface can be moved.
	 * 
	 * @return True if this surface can be moved, false if not.
	 *************************************** 
	 */
	boolean isMovable();

	/***************************************
	 * Indicates if this surface can be resized.
	 * 
	 * @return True if this surface can be resized, false if not.
	 *************************************** 
	 */
	boolean isResizable();

	/***************************************
	 * Set the height delta when the height is changed.
	 * 
	 * @param heightIncrement
	 *            A height delta, implementation dependent but usually in
	 *            pixels.
	 *************************************** 
	 */
	void setHeightIncrement(final int heightIncrement);

	/***************************************
	 * Set the maximum height of this surface.
	 * 
	 * @param maxHeight
	 *            a height, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	void setMaxHeight(final int maxHeight);

	/***************************************
	 * Set the maximum width of this surface.
	 * 
	 * @param maxWidth
	 *            a width, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	void setMaxWidth(final int maxWidth);

	/***************************************
	 * Set the minimum height of this surface.
	 * 
	 * @param minHeight
	 *            a height, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	void setMinHeight(final int minHeight);

	/***************************************
	 * Set the minimum width of this surface.
	 * 
	 * @param minWidth
	 *            a width, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	void setMinWidth(final int minWidth);

	/***************************************
	 * Indicate if this surface is movable.
	 * 
	 * @param movable
	 *            True if this surface should be movable, false if not.
	 *************************************** 
	 */
	void setMovable(final boolean movable);

	/***************************************
	 * Indicate if this surface is resizable.
	 * 
	 * @param isResizable
	 *            True if this surface should be resizable, false if not.
	 *************************************** 
	 */
	void setResizable(final boolean isResizable);

	/***************************************
	 * Set the width delta when the width is changed.
	 * 
	 * @param widthIncrement
	 *            A width delta, implementation dependent but usually in pixels.
	 *************************************** 
	 */
	void setWidthIncrement(final int widthIncrement);

	/***************************************
	 * Make this surface's geometry match the geometry of the backing
	 * {@link DisplaySurface}.
	 *************************************** 
	 */
	void syncGeoToDisplaySurface();

	/***************************************
	 * Make this surface the source of all user input.
	 *************************************** 
	 */
	void setInputFocus();
}

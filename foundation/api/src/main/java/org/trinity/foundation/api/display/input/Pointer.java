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

package org.trinity.foundation.api.display.input;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplayArea;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.display.event.ButtonNotify;
import org.trinity.foundation.api.shared.Coordinate;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.common.util.concurrent.ListenableFuture;

@ExecutionContext(DisplayExecutor.class)
@ThreadSafe
public interface Pointer extends InputDevice {
	/***************************************
	 * The position of the pointer as seen from this <code>DisplaySurface</code>
	 * 's coordinate system.
	 *
	 * @return The pointer position {@link Coordinate}.
	 ***************************************
	 */
	ListenableFuture<Coordinate> getPointerCoordinate(@Nonnull DisplaySurface displaySurface);

	/***************************************
	 * Grab a {@link Button} of the bound {@link DisplayArea} or any of its
	 * children. Grabbing a {@link Button} will override the delivery of the
	 * corresponding {@link ButtonNotify} event so it is only delivered to the
	 * grabber instead of delivering it anyone that is interested.
	 * <p>
	 * This method is usually used to install mousebindings.
	 *
	 * @param grabButton
	 *            The {@link Button} that should be grabbed.
	 * @param withModifiers
	 *            The {@link InputModifiers} that should be active if a grab is
	 *            to take place.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> grabButton(	@Nonnull DisplaySurface displaySurface,
										@Nonnull Button grabButton,
										@Nonnull InputModifiers withModifiers);

	/***************************************
	 * Grab the entire pointing device of the bound {@link DisplayArea} or any
	 * of its children. Every {@link ButtonNotify} shall be redirected.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #grabButton(Button, InputModifiers)
	 ***************************************
	 */
	ListenableFuture<Void> grabPointer(@Nonnull DisplaySurface displaySurface);

	/***************************************
	 * Release the grab on the pointing device.
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #grabPointer()
	 ***************************************
	 */
	ListenableFuture<Void> ungrabPointer();

	/***************************************
	 * Release the grab on the specific {@link Button} with the specific
	 * {@link InputModifiers}.
	 *
	 * @param ungrabButton
	 *            The {@link Button} that will be ungrabbed.
	 * @param withModifiers
	 *            The {@link InputModifiers}.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 * @see #grabButton(Button, InputModifiers)
	 ***************************************
	 */
	ListenableFuture<Void> ungrabButton(@Nonnull DisplaySurface displaySurface,
										@Nonnull Button ungrabButton,
										@Nonnull InputModifiers withModifiers);
}

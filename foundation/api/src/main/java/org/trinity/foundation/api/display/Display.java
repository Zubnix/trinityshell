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

import java.util.List;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.concurrent.ThreadSafe;

//TODO documentation
/**
 *
 */
@ExecutionContext(DisplayExecutor.class)
@ThreadSafe
public interface Display extends AsyncListenable {

	/**
	 * The logical representation of the physical screen.
	 * @return The screen object.
	 */
	ListenableFuture<Screen> getScreen();

	/**
	 * A "snapshot" of client display surfaces. This collection does not
	 * include the root display area. The returned array only includes the
	 * client display surfaces at the time of the call. Any future creation or
	 * deletion of client display surfaces will not be reflected by the returned
	 * array.
	 *
	 * @return A collection of client {@link DisplaySurface}s.
	 */
	ListenableFuture<List<DisplaySurface>> getDisplaySurfaces();

	/***************************************
     * Orderly shut down this {@code Display}. All resources living on this
     * {@code Display} will be shut down as well.
     *
     * @return A {@link ListenableFuture} that indicates when the operation is
     *         done.
     ***************************************
     */
	ListenableFuture<Void> quit();
}

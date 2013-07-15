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

import java.util.List;

import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.common.util.concurrent.ListenableFuture;

// TODO documentation
@ExecutionContext(DisplayExecutor.class)
public interface Display extends AsyncListenable {

	ListenableFuture<Screen> getScreen();

	/**
	 * An array "snapshot" of client display surfaces. This array does not
	 * include the root display area. The returned array only includes the
	 * client display surfaces at the time of the call. Any future creation or
	 * deletion of client display surfaces will not be reflected by the returned
	 * array.
	 *
	 * @return A collection of client {@link DisplaySurface}s.
	 */
	ListenableFuture<List<DisplaySurface>> getClientDisplaySurfaces();

	/***************************************
	 * Orderly shut down this <code>Display</code>. All resources living on this
	 * <code>Display</code> will be shut down as well.
	 * <p>
	 * This method does not shut down the underlying native display, it merely
	 * closes the connection to the underlying native display.
	 * </p>
	 *
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> quit();
}

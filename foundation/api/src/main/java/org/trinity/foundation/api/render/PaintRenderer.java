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
package org.trinity.foundation.api.render;

import com.google.common.util.concurrent.ListenableFuture;

/***************************************
 * An an interface to submit asynchronous paint operations.
 * 
 *************************************** 
 */
public interface PaintRenderer {

	/***************************************
	 * Invoke the given {@link PaintRoutine} with the given caller context.
	 * Actual invocation of the {@code PaintRoutine} is done later in a separate
	 * dedicated paint thread.
	 * 
	 * @param callerContext
	 *            the calling instance.
	 * @param paintRoutine
	 *            a {@link PaintRoutine}
	 * @return a {@link ListenableFuture} result.
	 *************************************** 
	 */
	<R> ListenableFuture<R> invoke(	Object callerContext,
									PaintRoutine<R, PaintContext> paintRoutine);

}

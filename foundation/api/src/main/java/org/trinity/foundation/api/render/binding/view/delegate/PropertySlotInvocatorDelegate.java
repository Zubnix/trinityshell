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
package org.trinity.foundation.api.render.binding.view.delegate;

import java.lang.reflect.Method;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.bindkey.RenderExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

import com.google.common.util.concurrent.ListenableFuture;

/***************************************
 * Delegate to invoke a view method when a bound model property changes. This
 * delegate should be implemented for a specific widget toolkit.
 *
 * @see PropertySlots
 * @see PropertySlot
 * @see PropertyChanged
 ***************************************
 */
@ExecutionContext(RenderExecutor.class)
public interface PropertySlotInvocatorDelegate {

	/***************************************
	 * Invoke a view method with the given argument.
	 *
	 * @param view
	 *            The view who's method should be invoked.
	 * @param viewMethod
	 *            The method of the view to invoke.
	 * @param argument
	 *            The argument to use when invoking the method. This can be an
	 *            array of objects, as defined in the var args of
	 *            {@link Method#invoke(Object, Object...)}.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 ***************************************
	 */
	ListenableFuture<Void> invoke(	Object view,
									Method viewMethod,
									Object argument);
}

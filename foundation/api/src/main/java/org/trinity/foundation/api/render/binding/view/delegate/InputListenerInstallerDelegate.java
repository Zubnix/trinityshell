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

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.model.InputSlot;
import org.trinity.foundation.api.render.binding.model.delegate.InputSlotCallerDelegate;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.OwnerThread;

import com.google.common.util.concurrent.ListenableFuture;

/***************************************
 * A delegate to listen for specific user input on a view instance. When input
 * arrives, a corresponding {@link BoundInputEvent} should be created and passed
 * to the target view model. This delegate should be implemented for a specific
 * widget toolkit.
 * 
 * @see InputSlotCallerDelegate
 *************************************** 
 */
@OwnerThread("Render")
public interface InputListenerInstallerDelegate {

	/***************************************
	 * Install a new input listener. When receiving the desired input, a
	 * corresponding {@link BoundInputEvent} should be generated and delivered
	 * to the input event target.
	 * 
	 * @param inputType
	 *            The type of {@link Input} to listen for.
	 * @param view
	 *            The view instance to listen for input.
	 * @param inputEventTarget
	 *            The view model interested in the {@link BoundInputEvent}.
	 * @param inputSlotName
	 *            The name of the {@link InputSlot} that should be invoked when
	 *            input arrives.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 *************************************** 
	 */
	ListenableFuture<Void> installViewInputListener(Class<? extends Input> inputType,
													Object view,
													AsyncListenable inputEventTarget,
													String inputSlotName);

	/***************************************
	 * Remove a previously installed input listener.
	 * 
	 * @param inputType
	 *            The type of {@link Input} that was listened to.
	 * @param view
	 *            The view used to listen for input.
	 * @param inputEventTarget
	 *            The view model that was interested in the
	 *            {@link BoundInputEvent}.
	 * @param inputSlotName
	 *            The name of the {@link InputSlot} that would be invoked when
	 *            input arrives.
	 * @return A {@link ListenableFuture} that indicates when the operation is
	 *         done.
	 *************************************** 
	 */
	ListenableFuture<Void> removeViewInputListener(	Class<? extends Input> inputType,
													Object view,
													AsyncListenable inputEventTarget,
													String inputSlotName);
}

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
package org.trinity.foundation.api.render.binding.model.delegate;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.model.InputSlot;

import com.google.common.util.concurrent.ListenableFuture;

import javax.annotation.Nonnull;

/***************************************
 * A convenience service to call a method marked with {@link InputSlot} based on
 * widget toolkit specific input.
 *
 ***************************************
 */
public interface InputSlotCallerDelegate {

	/***************************************
	 * @param model
	 * @param methodName
	 * @param input
	 * @return A Boolean indicating if an input slot was called.
	 ***************************************
	 */
	ListenableFuture<Boolean> callInputSlot(@Nonnull Object model,
	                                        @Nonnull String methodName,
	                                        @Nonnull Input input);
}
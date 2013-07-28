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
package org.trinity.foundation.api.render.binding.view;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.render.binding.model.InputSlot;
import org.trinity.foundation.api.render.binding.model.delegate.InputSlotCallerDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.InputListenerInstallerDelegate;

/***************************************
 * Mark a view as a producer of {@link BoundInputEvent}s. It's up to the
 * {@link InputListenerInstallerDelegate} to make sure the correct input
 * listeners are installed and the correct {@code BoundInputEvent}s are received
 * by the model of the marked view.
 * <p>
 * An {@code InputSignal} is used as an argument of {@link InputSignals}.
 *
 * @see InputSlotCallerDelegate
 ***************************************
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface InputSignal {
	/***************************************
	 * The {@link Input} type to listen for.
	 *
	 * @return An {@link Input} (sub)class.
	 ***************************************
	 */
	Class<? extends Input> inputType();

	/***************************************
	 * The name of the {@link InputSlot} that should be invoked. The name of the
	 * {@code InputSlot} is the name of the method that it annotates.
	 *
	 * @return a method name.
	 ***************************************
	 */
	String name();

}

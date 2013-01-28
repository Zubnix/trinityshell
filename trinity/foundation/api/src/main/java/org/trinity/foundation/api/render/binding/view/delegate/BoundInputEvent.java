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
import org.trinity.foundation.api.render.binding.model.InputSlotCaller;

/***************************************
 * An input event meant to invoke a bound {@link InputSlot}.
 * 
 * @see InputSlotCaller
 *************************************** 
 */
public interface BoundInputEvent {

	/***************************************
	 * The {@link Input} from a view object.
	 * 
	 * @return
	 *************************************** 
	 */
	Input getInput();

	/***************************************
	 * The name of the method marked as an {@link InputSlot}.
	 * 
	 * @return a method name.
	 *************************************** 
	 */
	String getInputSlotName();
}
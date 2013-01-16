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
package org.trinity.foundation.api.render.binding.model;

import org.trinity.foundation.api.render.binding.view.delegate.BoundInputEvent;

/***************************************
 * A convenience service to call a method marked with {@link InputSlot} based on
 * a {@link BoundInputEvent}.
 * 
 *************************************** 
 */
public interface InputSlotCaller {

	/***************************************
	 * Call the method marked with {@link InputSlot} that matches the given
	 * {@link BoundInputEvent}.
	 * 
	 * @param model
	 *            Any object which has a method marked with {@link InputSlot}.
	 * @param boundInputEvent
	 *            A {@link BoundInputEvent}.
	 *************************************** 
	 */
	void callInputSlot(	Object model,
						BoundInputEvent boundInputEvent);
}
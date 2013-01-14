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
package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.display.event.InputNotifyEvent;
import org.trinity.foundation.api.display.input.PointerInput;

/****************************************
 * Creator of {@link BoundButtonInputEvent}s.
 * 
 *************************************** 
 */
public interface BoundButtonInputEventFactory {
	/***************************************
	 * Create a new {@code BoundButtonInputEvent}.
	 * 
	 * @param inputTarget
	 *            The event target as defined in
	 *            {@link DisplayEvent#getDisplayEventTarget()}
	 * @param pointerInput
	 *            The input as defined in {@link InputNotifyEvent#getInput()}
	 * @param inputSlotName
	 *            The input slot name as defined in
	 *            {@link BoundInputEvent#getInputSlotName()}
	 * @return a new {@code BoundButtonInputEvent}.
	 *************************************** 
	 */
	BoundButtonInputEvent createBoundButtonInputEvent(	final Object inputTarget,
														final PointerInput pointerInput,
														final String inputSlotName);
}
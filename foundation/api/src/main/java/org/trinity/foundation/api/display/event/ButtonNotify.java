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
package org.trinity.foundation.api.display.event;

import javax.annotation.concurrent.Immutable;

import org.trinity.foundation.api.display.input.PointerInput;

/**
 * Notifies that a mouse button's state has changed.
 */
@Immutable
public class ButtonNotify extends InputNotify<PointerInput> {

	/***************************************
	 * Create a new <code>ButtonNotify</code> with the given event target as the
	 * receiver of this event. Ideally, the receiving object should correspond
	 * to the object that was clicked.
	 * 
	 * @param displayEventTarget
	 *            The receiver of this event.
	 * @param pointerInput
	 *            The {@link PointerInput} details.
	 *************************************** 
	 */
	public ButtonNotify(final PointerInput pointerInput) {
		super(pointerInput);
	}
}

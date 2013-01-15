/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.api.display.event;

import org.trinity.foundation.api.display.input.Input;

// TODO documentation
/**
 * Represents general user input notification. This can be, for example, a
 * keyboard key that was pressed or a mouse button that was released.
 */
public class InputNotifyEvent<I extends Input> extends DisplayEvent {

	private final I input;

	/***************************************
	 * Create a new <code>InputNotifyEvent</code> that targets the given
	 * {@link DisplayEventTarget}. The input detail is described by the given
	 * {@link Input} argument.
	 * 
	 * @param displayEventTarget
	 *            A {@link DisplayEventTarget}
	 * @param input
	 *            an {@link Input}
	 *************************************** 
	 */
	public InputNotifyEvent(final Object displayEventTarget, final I input) {
		super(displayEventTarget);
		this.input = input;
	}

	/***************************************
	 * The object describing the user input.
	 * 
	 * @return {@link Input}
	 *************************************** 
	 */
	public I getInput() {
		return this.input;
	}
}

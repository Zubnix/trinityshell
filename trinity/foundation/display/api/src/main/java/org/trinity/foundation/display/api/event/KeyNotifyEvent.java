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
package org.trinity.foundation.display.api.event;

import org.trinity.foundation.input.api.KeyboardInput;

// TODO documentation
/**
 * A <code>KeyNotifyEvent</code> notifies that a keyboard key's state has
 * changed.
 * 
 * 
 */
public class KeyNotifyEvent extends InputNotifyEvent<KeyboardInput> {

	/***************************************
	 * Create a new <code>KeyNotifyEvent</code> with the given
	 * {@link DisplayEventSource} as the surface where the keyboard input
	 * originated.
	 * 
	 * @param displayEventSource
	 *            a {@link DisplayEventSource}
	 * @param input
	 *            {@link KeyboardInput}
	 *************************************** 
	 */
	public KeyNotifyEvent(final DisplayEventSource displayEventSource, final KeyboardInput input) {
		super(	displayEventSource,
				input);
	}
}

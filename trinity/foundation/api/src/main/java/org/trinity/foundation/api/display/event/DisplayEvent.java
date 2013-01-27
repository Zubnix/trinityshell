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

import org.trinity.foundation.api.display.DisplayServer;

/**
 * Information coming from a {@link DisplayServer}.
 * 
 */
public class DisplayEvent {

	private final Object displayEventTarget;

	/****************************************
	 * Construct a generic <code>DisplayEvent</code> with the given target
	 * object as the receiver.
	 * 
	 * @param displayEventTarget
	 *            the receiving object.
	 *************************************** 
	 */
	public DisplayEvent(final Object displayEventTarget) {
		this.displayEventTarget = displayEventTarget;
	}

	/****************************************
	 * The object that this event targets. The targeted object is the preferred
	 * object for handling this event.
	 * 
	 * @return An event target
	 *************************************** 
	 */
	public Object getDisplayEventTarget() {
		return this.displayEventTarget;
	}

	@Override
	public String toString() {
		return String.format(	"%s=>[%s]",
								getClass().getSimpleName(),
								getDisplayEventTarget());
	}
}

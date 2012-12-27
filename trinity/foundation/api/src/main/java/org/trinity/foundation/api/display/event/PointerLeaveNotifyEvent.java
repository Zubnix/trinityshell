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

/***************************************
 * Indicates when a pointer device has left the area of a
 * {@link DisplayEventTarget}.
 * 
 *************************************** 
 */
public class PointerLeaveNotifyEvent extends PointerVisitationNotifyEvent {

	/***************************************
	 * Construct a new <code>PointerLeaveNotifyEvent</code> that originated from
	 * the given {@link DisplayEventTarget}.
	 * 
	 * @param displayEventTarget
	 *            A {@link DisplayEventTarget}.
	 *************************************** 
	 */
	public PointerLeaveNotifyEvent(final DisplayEventTarget displayEventTarget) {
		super(displayEventTarget);
	}

}

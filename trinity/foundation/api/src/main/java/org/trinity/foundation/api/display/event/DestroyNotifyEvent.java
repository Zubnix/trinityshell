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

/**
 * Notifies that a display resource is destroyed.
 * 
 */
public class DestroyNotifyEvent extends DisplayEvent {

	/***************************************
	 * Create a new <code>DestroyNotifyEvent</code> with the given event target
	 * as the receiver of this event. Ideally, the receiving object should
	 * correspond with the display resource that was destroyed.
	 * 
	 * @param displayEventTarget
	 *            the receiving object. eg the display resource that was
	 *            destroyed.
	 *************************************** 
	 */
	public DestroyNotifyEvent(final Object displayEventTarget) {
		super(displayEventTarget);
	}
}

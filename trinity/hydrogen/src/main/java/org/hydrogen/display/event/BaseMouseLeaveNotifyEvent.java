/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.display.event;

import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.MouseEnterLeaveNotifyEvent;

// TODO documentation
/**
 * A <code>BaseMouseLeaveNotifyEvent</code> is a basic implementation of a
 * <code>MouseEnterLeaveNotifyEvent</code>. Classes wishing to implement
 * <code>MouseEnterLeaveNotifyEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class BaseMouseLeaveNotifyEvent extends BaseDisplayEvent implements
		MouseEnterLeaveNotifyEvent {

	/**
	 * 
	 * @param eventSource
	 */
	public BaseMouseLeaveNotifyEvent(final DisplayEventSource eventSource) {
		super(DisplayEventType.MOUSE_LEAVE, eventSource);
	}
}

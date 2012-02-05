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
package org.hydrogen.displayinterface.event;

//TODO documentation
/**
 * A <code>BaseFocusOutNotifyEvent</code> is a basic implementation of a
 * <code>FocusNotifyEvent</code>. Classes wishing to implement
 * <code>FocusNotifyEvent</code> can use this class as a base.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class BaseFocusOutNotifyEvent extends BaseDisplayEvent implements
		FocusNotifyEvent {

	/**
	 * 
	 * @param eventSource
	 */
	public BaseFocusOutNotifyEvent(final DisplayEventSource eventSource) {
		super(FocusNotifyEvent.OUT_TYPE, eventSource);
	}
}

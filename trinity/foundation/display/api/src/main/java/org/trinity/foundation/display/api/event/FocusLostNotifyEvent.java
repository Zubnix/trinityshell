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
package org.trinity.foundation.display.api.event;

/***************************************
 * Indicates that a {@link DisplayEventSource} has lost the input focus.
 * 
 *************************************** 
 */
public class FocusLostNotifyEvent extends FocusNotifyEvent {

	/***************************************
	 * Create a new <code>FocusGainNotifyEvent</code> with the provided instance
	 * as the unfocused {@link DisplayEventSource}.
	 * 
	 * @param displayEventSource
	 *            An {@link DisplayEventSource}
	 *************************************** 
	 */
	public FocusLostNotifyEvent(final DisplayEventSource displayEventSource) {
		super(displayEventSource);
	}

}

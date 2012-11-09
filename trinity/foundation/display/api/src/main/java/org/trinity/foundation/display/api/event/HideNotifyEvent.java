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

/**
 * An <code>HideNotifyEvent</code> notifies when a {@link DisplayEventSource} is
 * made invisible on the screen.
 * 
 */
public class HideNotifyEvent extends DisplayEvent {

	/*****************************************
	 * Construct a new <code>HideNotifyEvent</code> that originated from the
	 * given {@link DisplayEventSource}.
	 * 
	 * @param displayEventSource
	 *            a {@link DisplayEventSource}.
	 ****************************************/
	public HideNotifyEvent(final DisplayEventSource displayEventSource) {
		super(displayEventSource);
	}
}

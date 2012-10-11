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

// TODO documentation
// TODO is this interface implemented in fusion-x11? If not, it should.
/**
 * A <code>StackingNotifyEvent</code> notifies that the stacking position of an
 * on-screen display resource, like a window, has changed
 * 
 */
public class StackingChangedNotifyEvent extends DisplayEvent {

	public StackingChangedNotifyEvent(final DisplayEventSource displayEventSource) {
		super(displayEventSource);
	}

}
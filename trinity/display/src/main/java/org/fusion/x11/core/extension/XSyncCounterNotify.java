/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core.extension;

import org.trinity.core.display.api.event.DisplayEvent;
import org.trinity.core.display.api.event.DisplayEventType;

//currently unused
//TODO documentation
/**
* Currently unused.
* 
* @author Erik De Rijcke
* @since 1.1
*/
public interface XSyncCounterNotify extends DisplayEvent {
	public static DisplayEventType TYPE = new DisplayEventType();

	@Override
	public XSyncSystemCounter getEventSource();

	long getWaitValue();

	long getCounterValue();

	boolean isCounterDestroyed();

	// int type; /*event base + XSyncCounterNotify */
	// unsigned long serial; /*number of last request processed by server */
	// Bool send event; /*true if this came from a SendEvent request */
	// Display *display; /*Display the event was read from */
	// XSyncSystemCounter counter; /* counter involved in await */
	// XSyncValue wait_value; /* value being waited for */
	// XSyncValue counter_value; /* counter value when this event was sent */
	// Time time; /* milliseconds */
	// int count; /* how many more events to come */
	// Bool destroyed; /* True if counter was destroyed */
}
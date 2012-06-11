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
package org.trinity.display.x11.api.extension.sync;

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
public interface XSyncAlarmNotify extends DisplayEvent {
	public static DisplayEventType TYPE = new DisplayEventType();

	@Override
	public XSyncAlarm getEventSource();

	long getCounterValue();

	long getAlarmValue();

	XSyncAlarmState getAlarmState();

	// int type; /*event base + XSyncAlarmNotify */
	// unsigned long serial; /*number of last request processed by server */
	// Bool send event; /*true if this came from a SendEvent request */
	// Display * display; /*Display the event was read from */
	// XSyncAlarm alarm; /*alarm that triggered */
	// XSyncValue counter_value; /* value that triggered the alarm */
	// XSyncValue alarm_value; /* test value of trigger in alarm */
	// Time time; /* milliseconds */
	// XSyncAlarmState state; /* new state of alarm */
}
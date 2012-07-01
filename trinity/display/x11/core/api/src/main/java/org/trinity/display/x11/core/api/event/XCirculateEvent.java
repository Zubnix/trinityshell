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
package org.trinity.display.x11.core.api.event;

import org.trinity.display.x11.core.api.XWindow;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public interface XCirculateEvent extends XEvent {
	// uint8_t response_type; /**< */
	// uint8_t pad0; /**< */
	// uint16_t sequence; /**< */
	// xcb_window_t event; /**< */
	// xcb_window_t window; /**< */
	// uint8_t pad1[4]; /**< */
	// uint8_t place; /**< */
	// uint8_t pad2[3]; /**< */
	int getSequence();

	XWindow getEvent();

	XWindow getWindow();

	int getPlace();

}

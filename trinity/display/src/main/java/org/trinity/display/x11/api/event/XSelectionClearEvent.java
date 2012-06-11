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
package org.trinity.display.x11.api.event;

import org.trinity.display.x11.api.XAtom;
import org.trinity.display.x11.api.XWindow;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public interface XSelectionClearEvent extends XEvent {
	// contents of native buffer:
	// uint8_t pad0; /**< */
	// uint16_t sequence; /**< */
	// xcb_timestamp_t time; /**< */
	// xcb_window_t owner; /**< */
	// xcb_atom_t selection; /**< */

	int getSequence();

	int getTime();

	XWindow getOwner();

	XAtom getSelection();
}

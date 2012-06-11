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

//currently unused
//TODO documentation
/**
* Currently unused.
* 
* @author Erik De Rijcke
* @since 1.1
*/
public interface XSyncTrigger {
	// XSyncCounter counter; /* counter to trigger on */
	// XSyncValueType value_type; /* absolute/relative */
	// XSyncValue wait_value; /* value to compare counter to */
	// XSyncTestType test_type; /* pos/neg comparison/transtion */

	long getCounter();

	XSyncValueType getValueType();

	long getWaitValue();

	XSyncTestType getTestType();
}

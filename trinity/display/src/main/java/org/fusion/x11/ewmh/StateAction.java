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
package org.fusion.x11.ewmh;

//TODO documentation
/**
* 
* @author Erik De Rijcke
* @since 1.0
*/
public enum StateAction {
	
	/**
	 * 
	 */
	_NET_WM_STATE_REMOVE, //ordinal = native value = 0 /* remove/unset property */

	/**
	 * 
	 */
	_NET_WM_STATE_ADD, //ordinal = native value =  1 /* add/set property */

	/**
	 * 
	 */
	_NET_WM_STATE_TOGGLE, //ordinal = native value =  2 /* toggle property */
}

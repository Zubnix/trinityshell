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

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public enum WmMoveResize {
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_TOPLEFT, // ordinal = native value = 0

	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_TOP, // ordinal = native value =1

	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_TOPRIGHT, // ordinal = native value =2

	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_RIGHT, // ordinal = native value =3

	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_BOTTOMRIGHT, // ordinal = native value =4

	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_BOTTOM, // ordinal = native value =5
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_BOTTOMLEFT, // ordinal = native value =6

	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_LEFT, // ordinal = native value =7

	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_MOVE, // ordinal = native value =8 /* movement only */

	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_KEYBOARD, // ordinal = native value =9 /* size via
										// keyboard */
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_MOVE_KEYBOARD, // ordinal = native value =10 /* move via
										// keyboard */
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_CANCEL, // ordinal = native value =11 /* cancel operation
								// */
}

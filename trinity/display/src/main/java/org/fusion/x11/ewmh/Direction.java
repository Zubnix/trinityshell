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
public enum Direction {
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_TOPLEFT,
	
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_TOP,
	
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_TOPRIGHT,
	
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_RIGHT,
	
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_BOTTOMRIGHT,
	
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_BOTTOM,
	
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_BOTTOMLEFT,
	
	/**
	 * 
	 */
	_NET_WM_MOVERESIZE_SIZE_LEFT,

	/**
	 * movement only
	 */
	_NET_WM_MOVERESIZE_MOVE

	/**
	 * size via keyboard
	 */
	, _NET_WM_MOVERESIZE_SIZE_KEYBOARD,

	/**
	 * move via keyboard
	 */
	_NET_WM_MOVERESIZE_MOVE_KEYBOARD,
	
	/**
	 * cancel operation
	 */
	_NET_WM_MOVERESIZE_CANCEL
}

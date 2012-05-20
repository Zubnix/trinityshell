/*
 * This file is part of Hydrogen.
 * 
 * Hydrogen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Hydrogen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Hydrogen. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hydrogen.display.api.event;

import org.hydrogen.event.api.Type;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public enum DisplayEventType implements Type {
	/**
	 * 
	 */
	BUTTON_PRESSED,
	/**
	 * 
	 */
	BUTTON_RELEASED,
	/**
	 * 
	 */
	CLIENT_MESSAGE,
	/*
	 * 
	 */
	CONFIGURE_NOTIFY,
	/**
	 * 
	 */
	CONFIGURE_REQUEST,
	/**
	 * 
	 */
	DESTROY_NOTIFY,
	/**
	 * 
	 */
	FOCUS_GAIN_NOTIFY,
	/**
	 * 
	 */
	FOCUS_LOST_NOTIFY,
	/**
	 * 
	 */
	KEY_PRESSED,
	/**
	 * 
	 */
	KEY_RELEASED,
	/**
	 * 
	 */
	MAP_NOTIFY,
	/**
	 * 
	 */
	MAP_REQUEST,
	/**
	 * 
	 */
	MOUSE_ENTER,
	/**
	 * 
	 */
	MOUSE_LEAVE,
	/**
	 * 
	 */
	PROPERTY_CHANGED,
	/**
	 * 
	 */
	STACKING_CHANGED,
	/**
	 * 
	 */
	UNMAP_NOTIFY
}

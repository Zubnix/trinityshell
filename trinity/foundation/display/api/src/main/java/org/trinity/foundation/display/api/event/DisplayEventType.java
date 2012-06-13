/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.api.event;

import org.trinity.core.event.api.Type;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class DisplayEventType implements Type {
	/**
	 * 
	 */
	public static final DisplayEventType BUTTON_PRESSED = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType BUTTON_RELEASED = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType CLIENT_MESSAGE = new DisplayEventType();
	/*
	 * 
	 */
	public static final DisplayEventType CONFIGURE_NOTIFY = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType CONFIGURE_REQUEST = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType DESTROY_NOTIFY = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType FOCUS_GAIN_NOTIFY = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType FOCUS_LOST_NOTIFY = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType KEY_PRESSED = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType KEY_RELEASED = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType MAP_NOTIFY = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType MAP_REQUEST = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType MOUSE_ENTER = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType MOUSE_LEAVE = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType PROPERTY_CHANGED = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType STACKING_CHANGED = new DisplayEventType();
	/**
	 * 
	 */
	public static final DisplayEventType UNMAP_NOTIFY = new DisplayEventType();
}

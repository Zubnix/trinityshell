/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.api.geo;

import org.hydrogen.api.event.Type;

/**
 * A <code>GeoOperation</code> is a type of a <code>GeoEvent</code>. The
 * different instances of a <code>GeoOperation</code> are defined in the
 * {@link GeoEvent} class.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public enum GeoOperation implements Type {
	/**
	 * 
	 */
	LOWER,
	/**
	 * 
	 */
	LOWER_REQUEST,
	/**
	 * 
	 */
	MOVE,
	/**
	 * 
	 */
	MOVE_REQUEST,
	/**
	 * 
	 */
	MOVE_RESIZE,

	/**
	 * 
	 */
	MOVE_RESIZE_REQUEST,

	/**
	 * 
	 */
	RAISE,

	/**
	 * 
	 */
	RAISE_REQUEST,

	/**
	 * 
	 */
	REPARENT,

	/**
	 * 
	 */
	REPARENT_REQUEST,

	/**
	 * 
	 */
	RESIZE,

	/**
	 * 
	 */
	RESIZE_REQUEST,

	/**
	 * 
	 */
	VISIBILITY,

	/**
	 * 
	 */
	VISIBILITY_REQUEST,

	/**
	 * 
	 */
	DESTROYED,
	/**
	 * 
	 */
	PARENT_NOTIFY,
	/**
	 * 
	 */
	CHILD_LEFT_NOTIFY
}

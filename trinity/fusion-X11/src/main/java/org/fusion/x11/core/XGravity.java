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
package org.fusion.x11.core;

import java.util.HashMap;
import java.util.Map;

//currently unused
//TODO documentation
/**
 * Currently unused.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XGravity {

	private static final Map<Integer, XGravity> GRAVITY_MAP = new HashMap<Integer, XGravity>();

	/**
	 * 
	 */
	public static final XGravity northWestGravity = new XGravity(
			XProtocolConstants.GRAVITY_NORTH_WEST);

	/**
	 * 
	 */
	public static final XGravity northGravity = new XGravity(
			XProtocolConstants.GRAVITY_NORTH);

	/**
	 * 
	 */
	public static final XGravity northEastGravity = new XGravity(
			XProtocolConstants.GRAVITY_NORTH_EAST);

	/**
	 * 
	 */
	public static final XGravity westGravity = new XGravity(
			XProtocolConstants.GRAVITY_WEST);

	/**
	 * 
	 */
	public static final XGravity centerGravity = new XGravity(
			XProtocolConstants.GRAVITY_CENTER);

	/**
	 * 
	 */
	public static final XGravity eastGravity = new XGravity(
			XProtocolConstants.GRAVITY_EAST);

	/**
	 * 
	 */
	public static final XGravity southWestGravity = new XGravity(
			XProtocolConstants.GRAVITY_SOUTH_WEST);

	/**
	 * 
	 */
	public static final XGravity southGravity = new XGravity(
			XProtocolConstants.GRAVITY_SOUTH);

	/**
	 * 
	 */
	public static final XGravity southEastGravity = new XGravity(
			XProtocolConstants.GRAVITY_SOUTH_EAST);

	private final int gravity;

	private XGravity(final int gravity) {
		this.gravity = gravity;
		XGravity.GRAVITY_MAP.put(Integer.valueOf(gravity), this);
	}

	/**
	 * 
	 * @return
	 */
	public final int getGravity() {
		return this.gravity;
	}

	/**
	 * 
	 * @param gravityCode
	 * @return
	 */
	public static XGravity findGravity(final int gravityCode) {
		final XGravity gravity = XGravity.GRAVITY_MAP.get(Integer
				.valueOf(gravityCode));
		if (gravity == null) {
			return XGravity.northWestGravity;
		} else {
			return gravity;
		}
	}
}

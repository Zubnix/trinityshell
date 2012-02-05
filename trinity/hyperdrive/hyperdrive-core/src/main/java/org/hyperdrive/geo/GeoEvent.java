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
package org.hyperdrive.geo;

import org.hydrogen.eventsystem.Event;

// TODO documentation
/**
 * A <code>GeoEvent</code> carries the information that describes a geometry
 * operation.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class GeoEvent implements Event<GeoOperation> {

	/**
	 * 
	 */
	public static final GeoOperation LOWER = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation LOWER_REQUEST = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation MOVE = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation MOVE_REQUEST = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation MOVE_RESIZE = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation MOVE_RESIZE_REQUEST = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation RAISE = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation RAISE_REQUEST = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation REPARENT = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation REPARENT_REQUEST = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation RESIZE = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation RESIZE_REQUEST = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation VISIBILITY = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation VISIBILITY_REQUEST = new GeoOperation();

	/**
	 * 
	 */
	public static final GeoOperation DESTROYED = new GeoOperation();

	private final GeoOperation operation;
	private final GeoTransformableRectangle transformableSquare;
	private final GeoTransformation transformation;

	/**
	 * 
	 * @param geoOperation
	 * @param transformableSquare
	 * @param transformation
	 */
	public GeoEvent(final GeoOperation geoOperation,
			final GeoTransformableRectangle transformableSquare,
			final GeoTransformation transformation) {
		this.operation = geoOperation;
		this.transformableSquare = transformableSquare;
		this.transformation = transformation;
	}

	/**
	 * 
	 * @return
	 */
	public GeoTransformableRectangle getTransformableSquare() {
		return this.transformableSquare;
	}

	/**
	 * 
	 * @return
	 */
	public GeoTransformation getTransformation() {
		return this.transformation;
	}

	@Override
	public GeoOperation getType() {
		return this.operation;
	}
}

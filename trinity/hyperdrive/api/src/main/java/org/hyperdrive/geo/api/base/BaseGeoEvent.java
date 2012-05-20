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
package org.hyperdrive.geo.api.base;

import org.hyperdrive.geo.api.GeoEvent;
import org.hyperdrive.geo.api.GeoOperation;
import org.hyperdrive.geo.api.GeoTransformableRectangle;
import org.hyperdrive.geo.api.GeoTransformation;

// TODO documentation
/**
 * A <code>GeoEvent</code> carries the information that describes a geometry
 * operation.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class BaseGeoEvent implements GeoEvent {

	private final GeoOperation operation;
	private final GeoTransformableRectangle transformableSquare;
	private final GeoTransformation transformation;

	/**
	 * 
	 * @param geoOperation
	 * @param transformableSquare
	 * @param transformation
	 */
	public BaseGeoEvent(final GeoOperation geoOperation,
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
	@Override
	public GeoTransformableRectangle getGeoTransformableRectangle() {
		return this.transformableSquare;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public GeoTransformation getGeoTransformation() {
		return this.transformation;
	}

	@Override
	public GeoOperation getType() {
		return this.operation;
	}
}

/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.geo.impl;

import org.trinity.shell.geo.api.GeoEvent;
import org.trinity.shell.geo.api.GeoOperation;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.GeoTransformation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

// TODO documentation
/**
 * A <code>GeoEvent</code> carries the information that describes a geometry
 * operation.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class GeoEventImpl implements GeoEvent {

	private final GeoOperation operation;
	private final GeoTransformableRectangle transformableSquare;
	private final GeoTransformation transformation;

	/**
	 * @param geoOperation
	 * @param transformableSquare
	 * @param transformation
	 */
	@Inject
	protected GeoEventImpl(	@Assisted final GeoOperation geoOperation,
							@Assisted final GeoTransformableRectangle transformableSquare,
							@Assisted final GeoTransformation transformation) {
		this.operation = geoOperation;
		this.transformableSquare = transformableSquare;
		this.transformation = transformation;
	}

	/**
	 * @return
	 */
	@Override
	public GeoTransformableRectangle getGeoTransformableRectangle() {
		return this.transformableSquare;
	}

	/**
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

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
package org.hyperdrive.geo.api;

// TODO documentation
/**
 * A <code>GeoManager</code> implements logic to layout one or more
 * <code>GeoTransformableRectangle</code> s.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public interface GeoManager {

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onResizeRequest(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onMoveRequest(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onMoveResizeRequest(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onLowerRequest(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onRaiseRequest(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onChangeVisibilityRequest(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onChangeParentRequest(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onResizeNotify(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onMoveNotify(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onMoveResizeNotify(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onLowerNotify(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onRaiseNotify(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onChangeVisibilityNotify(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onChangeParentNotify(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);

	void onDestroyNotify(GeoTransformableRectangle geoTransformable,
			GeoTransformation transformation);
}

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
package org.trinity.shell.geo.api.manager;

import org.trinity.shell.geo.api.event.GeoEvent;

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
	void onResizeRequest(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onMoveRequest(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onMoveResizeRequest(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onLowerRequest(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onRaiseRequest(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onChangeParentRequest(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onResizeNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onMoveNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onMoveResizeNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onLowerNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onRaiseNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 * 
	 */
	void onChangeParentNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 */
	void onDestroyNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 */
	void onShowNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 */
	void onShowRequest(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 */
	void onHideNotify(GeoEvent geoEvent);

	/**
	 * 
	 * @param geoTransformable
	 * @param transformation
	 */
	void onHideRequest(GeoEvent geoEvent);
}

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

/**
 * A <code>HasGeoManager</code> is implemented by a
 * <code>GeoTransformableRectangle</code> to indicate that it has the authority,
 * as defined by the returned {@link GeoManager}, to handle a child's geometry
 * request. A child's geometry request is only handled if there is no other
 * intermediate child that implements <code>HasGeoManager</code> else that
 * intermediate <code>child</code> will handle the geometry request.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface HasGeoManager {

	/**
	 * The <code>GeoManager</code> that will handle a child's geometry request.
	 * 
	 * @return The <code>GeoManger</code> that implements a certain layout.
	 */
	GeoManager getGeoManager();
}

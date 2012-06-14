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
package org.trinity.shell.geo.api;

/**
 * A <code>GeoTransformable</code> can have it's geometry changed over time.
 * <p>
 * To access the desired transformation of a <code>GeoTransformable</code>,
 * implementing classes must provide a <code>GeoTransformation</code> describing
 * the current and new geometric values of the <code>GeoTransformable</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface GeoTransformable {
	/**
	 * The <code>GeoTransformation</code> of this <code>GeoTransformable</code>
	 * at the time of the call.
	 * <p>
	 * A <code>GeoTransformation</code> describes how this
	 * <code>GeoTransformable</code> wishes to be transformed.
	 * 
	 * @return A {@link GeoTransformation}.
	 */
	GeoTransformation toGeoTransformation();
}

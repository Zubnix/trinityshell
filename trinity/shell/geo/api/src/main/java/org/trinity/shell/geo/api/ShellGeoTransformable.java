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
 * A <code>ShellGeoTransformable</code> can have it's geometry changed over time.
 * <p>
 * To access the desired transformation of a <code>ShellGeoTransformable</code>,
 * implementing classes must provide a <code>ShellGeoTransformation</code> describing
 * the current and new geometric values of the <code>ShellGeoTransformable</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface ShellGeoTransformable {
	/**
	 * The <code>ShellGeoTransformation</code> of this <code>ShellGeoTransformable</code>
	 * at the time of the call.
	 * <p>
	 * A <code>ShellGeoTransformation</code> describes how this
	 * <code>ShellGeoTransformable</code> wishes to be transformed.
	 * 
	 * @return A {@link ShellGeoTransformation}.
	 */
	ShellGeoTransformation toGeoTransformation();
}

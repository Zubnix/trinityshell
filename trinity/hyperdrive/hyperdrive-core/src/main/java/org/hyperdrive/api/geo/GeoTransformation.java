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

public interface GeoTransformation {
	int getDeltaHeight();

	int getDeltaWidth();

	int getDeltaX();

	int getDeltaY();

	int getHeight0();

	int getHeight1();

	GeoTransformableRectangle getParent0();

	GeoTransformableRectangle getParent1();

	int getWidth0();

	int getWidth1();

	int getX0();

	int getX1();

	int getY0();

	int getY1();

	boolean isVisible0();

	boolean isVisible1();
}

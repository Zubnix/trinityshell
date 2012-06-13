/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.core.geometry.api;

import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public interface GeometryFactory {
	/*****************************************
	 * @param x
	 * @param y
	 * @return
	 *************************************** 
	 */
	Coordinates createCoordinates(@Assisted("x") int x, @Assisted("y") int y);

	/*****************************************
	 * @param coordinates
	 * @return
	 *************************************** 
	 */
	Coordinates createCoordinates(Coordinates coordinates);

	/*****************************************
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 *************************************** 
	 */
	Rectangle createRectangle(	@Assisted("x") int x,
								@Assisted("y") int y,
								@Assisted("width") int width,
								@Assisted("height") int height);

	/*****************************************
	 * @param rectangle
	 * @return
	 *************************************** 
	 */
	Rectangle createRectangle(Rectangle rectangle);

	/*****************************************
	 * @param coordinates
	 * @param width
	 * @param height
	 * @return
	 *************************************** 
	 */
	Rectangle createRectangle(	Coordinates coordinates,
								@Assisted("width") int width,
								@Assisted("height") int height);
}

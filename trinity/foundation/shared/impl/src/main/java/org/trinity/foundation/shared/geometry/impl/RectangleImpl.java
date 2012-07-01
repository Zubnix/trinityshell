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
package org.trinity.foundation.shared.geometry.impl;

import org.trinity.foundation.shared.geometry.api.Coordinates;
import org.trinity.foundation.shared.geometry.api.Rectangle;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class RectangleImpl implements Rectangle {

	private final int x;
	private final int y;
	private final int width;
	private final int height;

	@AssistedInject
	public RectangleImpl(	@Assisted("x") final int x,
							@Assisted("y") final int y,
							@Assisted("width") final int width,
							@Assisted("height") final int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@AssistedInject
	public RectangleImpl(	@Assisted final Coordinates coordinates,
							@Assisted("width") final int width,
							@Assisted("height") final int height) {
		this(	coordinates.getX(),
				coordinates.getY(),
				width,
				height);
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.geometry.api.Rectangle#getX()
	 */
	@Override
	public int getX() {
		return this.x;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.geometry.api.Rectangle#getY()
	 */
	@Override
	public int getY() {
		return this.y;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.geometry.api.Rectangle#getWidth()
	 */
	@Override
	public int getWidth() {
		return this.width;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.core.geometry.api.Rectangle#getHeight()
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

}

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
package org.trinity.foundation.shared.geometry.api;

/***************************************
 * A fixed size, fixed width {@link Rectangle}.
 * 
 *************************************** 
 */
public class ImmutableRectangle implements Rectangle {

	private final int x, y, width, height;

	public ImmutableRectangle(final Rectangle rectangle) {
		this(	rectangle.getX(),
				rectangle.getY(),
				rectangle.getWidth(),
				rectangle.getHeight());
	}

	public ImmutableRectangle(final Coordinate position, final int width, final int height) {
		this(	position.getX(),
				position.getY(),
				width,
				height);
	}

	public ImmutableRectangle(final int x, final int y, final int width, final int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}
}

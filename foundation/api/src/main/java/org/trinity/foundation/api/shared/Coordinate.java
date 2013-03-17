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
package org.trinity.foundation.api.shared;

import javax.annotation.concurrent.Immutable;

/***************************************
 * A coordinate in 2D space, in natural numbers.
 *************************************** 
 */
@Immutable
public class Coordinate {

	private final int x, y;

	/**
	 * Create a new {@code Coordinate} with the given X and Y value.
	 * 
	 * @param x
	 *            an int, depicting the horizontal position on the screen.
	 * @param y
	 *            an int, depicting the vertical position on the screen.
	 */
	public Coordinate(	final int x,
						final int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a new {@code Coordinate} with the same values as the given
	 * {@code Coordinate}.
	 * 
	 * @param coordinates
	 *            The {@link Coordinate} who's values to copy.
	 */
	public Coordinate(final Coordinate coordinates) {
		this(	coordinates.getX(),
				coordinates.getY());
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public Coordinate add(final Coordinate coordinate) {
		return new Coordinate(	getX() + coordinate.getX(),
								getY() + coordinate.getY());
	}

	public Coordinate subtract(final Coordinate coordinate) {
		return new Coordinate(	getX() - coordinate.getX(),
								getY() - coordinate.getY());
	}
}

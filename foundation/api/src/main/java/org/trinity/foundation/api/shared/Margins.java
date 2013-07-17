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

import javax.annotation.Nonnegative;
import javax.annotation.concurrent.Immutable;

/**
 *
 * The extra space between an object and it's neighbors.
 *
 */
@Immutable
public class Margins {

	/**
	 * Short for {@code new Margins(0,0,0,0)}
	 */
	public static final Margins NO_MARGINS = new Margins(0);

	private final int bottom, left, right, top;

	/**
	 * Short for {@code new Margins(m,m,m,m)}
	 *
	 * @param m
	 *            margin size to use for all borders (left, right,bottom,top).
	 */
	public Margins(@Nonnegative final int m) {
		this(	m,
				m);
	}

	/**
	 * Short for {@code new Margins(h,h,v,v)}
	 *
	 * @param h
	 *            horizontal size to use for horizontal borders (left,right)
	 * @param v
	 *            vertical size to use for vertical borders (bottom,top)
	 */
	public Margins(@Nonnegative	final int h,
                   @Nonnegative final int v) {
		this(	h,
				h,
				v,
				v);
	}

	/**
	 * Create new {@code Margins} with a given left, right, bottom and top
	 * distance.
	 *
	 * @param left
	 *            The distance on the left between the object and it's neighbor.
	 * @param right
	 *            The distance to the right between the object and it's
	 *            neighbor.
	 * @param bottom
	 *            The distance at the bottom between the object and it's
	 *            neighbor.
	 * @param top
	 *            The distance at the top between the object and it's neighbor.
	 */
	public Margins(@Nonnegative	final int left,
                   @Nonnegative	final int right,
                   @Nonnegative	final int bottom,
                   @Nonnegative	final int top) {
		this.bottom = bottom;
		this.right = right;
		this.left = left;
		this.top = top;
	}

	/**
	 * The distance at the bottom between the object and it's neighbor.
	 *
	 * @return a distance
	 */
	public int getBottom() {
		return this.bottom;
	}

	/**
	 * The distance on the left between the object and it's neighbor.
	 *
	 * @return a distance
	 */
	public int getLeft() {
		return this.left;
	}

	/**
	 * The distance to the right between the object and it's neighbor.
	 *
	 * @return a distance
	 */
	public int getRight() {
		return this.right;
	}

	/**
	 * The distance at the top between the object and it's neighbor.
	 *
	 * @return a distance
	 */
	public int getTop() {
		return this.top;
	}
}
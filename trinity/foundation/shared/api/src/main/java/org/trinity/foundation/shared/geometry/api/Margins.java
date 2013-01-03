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

public class Margins {

	private final int bottom, left, right, top;

	public Margins(final int margins) {
		this(	margins,
				margins);
	}

	public Margins(final int horiz, final int vert) {
		this(	horiz,
				horiz,
				vert,
				vert);
	}

	public Margins(final int left, final int right, final int bottom, final int top) {
		this.bottom = bottom;
		this.right = right;
		this.left = left;
		this.top = top;
	}

	public int getBottom() {
		return this.bottom;
	}

	public int getLeft() {
		return this.left;
	}

	public int getRight() {
		return this.right;
	}

	public int getTop() {
		return this.top;
	}
}
/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.foundation.api.shared;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/***************************************
 * A fixed size, fixed width {@link Rectangle}.
 ***************************************
 */
@Immutable
public class ImmutableRectangle implements Rectangle {

	private final Coordinate position;
	private final Size size;

	/**
	 * Create a new {@code ImmutableRectangle} with the same geometry as the
	 * given {@code Rectangle}.
	 *
	 * @param rectangle
	 *            a {@link Rectangle}
	 */
	public ImmutableRectangle(@Nonnull final Rectangle rectangle) {
		this(	rectangle.getPosition(),
				rectangle.getSize());
	}

	/**
	 * Create a new {@code ImmutableRectangle} with the given {@code Coordinate}
	 * as the position, and the given width & height as its dimension.
	 *
	 * @param position
	 *            A {@link Coordinate}
	 * @param width
	 *            an int, depicting the horizontal size.
	 * @param height
	 *            an int, depicting the vertical size.
	 */
	public ImmutableRectangle(@Nonnull final Coordinate position,
								@Nonnegative final int width,
                                @Nonnegative final int height) {
		this(	position.getX(),
				position.getY(),
				width,
				height);
	}

	public ImmutableRectangle(	final int x,
								final int y,
                                @Nonnull final Size size) {
		this(	x,
				y,
				size.getWidth(),
				size.getHeight());
	}

	public ImmutableRectangle(@Nonnull	final Coordinate position,
                              @Nonnull	final Size size) {
		this(	position,
				size.getWidth(),
				size.getHeight());
	}

	/**
	 * Create a new {@code ImmutableRectangle} with the given x and y value as
	 * the position, and the given width & height as its dimension.
	 *
	 * @param x
	 *            an int, depicting the horizontal position.
	 * @param y
	 *            an int, depicting the vertical position.
	 * @param width
	 *            an int, depicting the horizontal size.
	 * @param height
	 *            an int, depicting the vertical size.
	 */
	public ImmutableRectangle(	final int x,
								final int y,
							@Nonnegative	final int width,
							@Nonnegative	final int height) {
		this.position = new Coordinate(	x,
										y);
		this.size = new Size(	width,
								height);
	}

	@Override
	public Coordinate getPosition() {
		return this.position;
	}

	@Override
	public Size getSize() {
		return this.size;
	}
}

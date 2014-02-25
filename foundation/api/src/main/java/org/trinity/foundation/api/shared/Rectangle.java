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

import com.google.auto.value.AutoValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * ************************************
 * A fixed size, fixed width {@link Rectangle}.
 * **************************************
 */
@Immutable
@AutoValue(cacheHashCode = true)
public abstract class Rectangle {

    public static Rectangle create(@Nonnull final Coordinate position,
                                   @Nonnull final Size size) {
        return new AutoValue_Rectangle(position, size);
    }

    /**
     * Create a new {@code Rectangle} with the given x and y value as
     * the position, and the given width &amp; height as its dimension.
     *
     * @param x      an int, depicting the horizontal position.
     * @param y      an int, depicting the vertical position.
     * @param width  an int, depicting the horizontal size.
     * @param height an int, depicting the vertical size.
     */
    public static Rectangle create(@Nonnull final Integer x,
                                   @Nonnull final Integer y,
                                   @Nonnull @Nonnegative final Integer width,
                                   @Nonnull @Nonnegative final Integer height) {
        return create(Coordinate.create(x, y), Size.create(width, height));
    }

    /**
     * Create a new {@code Rectangle} with the same geometry as the
     * given {@code Rectangle}.
     *
     * @param rectangle a {@link Rectangle}
     */
    public static Rectangle create(@Nonnull final Rectangle rectangle){
        return create(rectangle.getPosition(),
                rectangle.getSize());
    }

    /**
     * Create a new {@code Rectangle} with the given {@code Coordinate}
     * as the position, and the given width &amp; height as its dimension.
     *
     * @param position A {@link Coordinate}
     * @param width    an int, depicting the horizontal size.
     * @param height   an int, depicting the vertical size.
     */
    public static Rectangle create(@Nonnull final Coordinate position,
                                   @Nonnull @Nonnegative final Integer width,
                                   @Nonnull @Nonnegative final Integer height){
        return create(position.getX(),
                position.getY(),
                width,
                height);
    }

    public static Rectangle create(@Nonnull final Integer x,
                                   @Nonnull final Integer y,
                                   @Nonnull final Size size){
        return create(x,
                y,
                size.getWidth(),
                size.getHeight());
    }

    public abstract Coordinate getPosition();

    public abstract Size getSize();
}

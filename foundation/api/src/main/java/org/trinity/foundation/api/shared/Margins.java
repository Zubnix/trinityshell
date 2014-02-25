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
import com.google.common.base.Objects;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 *
 * The extra space between an object and it's neighbors.
 *
 */
@Immutable
@AutoValue(cacheHashCode = true)
public abstract class Margins {

	/**
	 * Short for {@code new Margins(0,0,0,0)}
	 */
	public static final Margins NO_MARGINS = Margins.create(0);

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
    public static Margins create(@Nonnull @Nonnegative	final Integer left,
                                 @Nonnull @Nonnegative	final Integer right,
                                 @Nonnull @Nonnegative	final Integer bottom,
                                 @Nonnull @Nonnegative	final Integer top){
        return new AutoValue_Margins(left, right, bottom, top);
    }

    /**
     * Short for {@code new Margins(h,h,v,v)}
     *
     * @param h
     *            horizontal size to use for horizontal borders (left,right)
     * @param v
     *            vertical size to use for vertical borders (bottom,top)
     */
    public static Margins create(@Nonnull @Nonnegative final Integer h,
                                 @Nonnull @Nonnegative final Integer v){
        return create(	h,
                        h,
                        v,
                        v);
    }

    /**
     * Short for {@code new Margins(m,m,m,m)}
     *
     * @param m
     *            margin size to use for all borders (left, right,bottom,top).
     */
    public static Margins create(@Nonnull @Nonnegative final Integer m){
        return create(m,
                m);
    }

	/**
	 * The distance at the bottom between the object and it's neighbor.
	 *
	 * @return a distance
	 */
	public abstract Integer getBottom();

	/**
	 * The distance on the left between the object and it's neighbor.
	 *
	 * @return a distance
	 */
	public abstract Integer getLeft();

	/**
	 * The distance to the right between the object and it's neighbor.
	 *
	 * @return a distance
	 */
	public abstract Integer getRight();

	/**
	 * The distance at the top between the object and it's neighbor.
	 *
	 * @return a distance
	 */
	public abstract Integer getTop();
}
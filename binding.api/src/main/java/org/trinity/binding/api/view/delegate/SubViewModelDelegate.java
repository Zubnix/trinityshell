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
package org.trinity.binding.api.view.delegate;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * ************************************
 * A delegate to handle the life cycle of a child view element. This delegate
 * should be implemented for a specific widget toolkit.
 * **************************************
 */
public interface SubViewModelDelegate {
    /**
     * ************************************
     * Create a new view element.
     *
     * @param parentView    The parent view of the new view instance.
     * @param childViewType The type of the new view instance.
     * @param position      The position (index) of the new view, relative to it's parent.
     * @param <T>           The type of the new view.
     * @return a future new view instance.
     * <p/>
     * **************************************
     */
    <T> T newView(@Nonnull Object parentView,
                  @Nonnull Class<T> childViewType,
                  @Nonnegative int position);

    /**
     * ************************************
     * Destroy a view element.
     *
     * @param parentView       The parent of the view that should be destroyed.
     * @param deletedChildView The view that should be destroyed.
     * @param deletedPosition  The index of the view that should be destroyed.
     *                         **************************************
     */
    void destroyView(@Nonnull Object parentView,
                     @Nonnull Object deletedChildView,
                     @Nonnegative int deletedPosition);

    /**
     * ************************************
     * Update the position (index) of a view element, relative to it's parent.
     *
     * @param parentView  The parent view.
     * @param childView   The view who's position should be updated.
     * @param oldPosition The old position.
     * @param newPosition The new position.
     *                    **************************************
     */
    void updateChildViewPosition(@Nonnull Object parentView,
                                 @Nonnull Object childView,
                                 @Nonnegative int oldPosition,
                                 @Nonnegative int newPosition);
}

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
package org.trinity.binding.api;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

/**
 * ************************************
 * Binds views to models.
 * <p/>
 * **************************************
 */
public interface ViewBinder {

    /**
     * ************************************
     * Update the bound views so they reflect the state of the changed data model.
     *
     * @param dataModel        The data model who's property changed.
     * @param propertyName     The name of the property.
     * @param oldPropertyValue The old value of the property.
     * @param newPropertyValue The new value of the property.
     *                         **************************************
     */
    void updateDataModelBinding(
            @Nonnull Object dataModel,
            @Nonnull String propertyName,
            @Nonnull Optional<Object> oldPropertyValue,
            @Nonnull Optional<Object> newPropertyValue);

    /**
     * ************************************
     * Bind a view model to a data model so a view reflects the data model's state. The given data model
     * model will be used as the root data model context for the given view model.
     *
     * @param dataModel The data model to visually represent
     * @param viewModel The visual representation of the data model
     *                  **************************************
     */
    void bind(
            @Nonnull Object dataModel,
            @Nonnull Object viewModel);

    void unbind(
            @Nonnull Object dataModel,
            @Nonnull Object viewModel);

    /**
     * Update a bound view so that the changed sub view is reflected.
     *
     * @param parentViewModel  The view model who's sub view changed.
     * @param subViewFieldName The name of the field that holds the sub view.
     * @param oldSubView       The field value of the old sub view.
     * @param newSubView       The field value of the new sub view.
     */
    void updateViewModelBinding(
            @Nonnull final Object parentViewModel,
            @Nonnull final String subViewFieldName,
            @Nonnull final Optional<?> oldSubView,
            @Nonnull final Optional<?> newSubView);
}
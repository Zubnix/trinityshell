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
package org.trinity.foundation.api.render.binding;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import javax.annotation.Nonnull;

/***************************************
 * Binds views to models.
 *
 ***************************************
 */
public interface ViewBinder {

	/***************************************
     * Update the bound views so they reflect the state of the changed data model.
     * @param dataModelExecutor The executor that operates on the data model.
     * @param changedDataModel The data model who's property changed.
     * @param propertyName The name of the property that changed.
     * @return A future indicating when the operation is done.
     ***************************************
     */
    ListenableFuture<Void> updateDataModelBinding(@Nonnull ListeningExecutorService dataModelExecutor,
                                                  @Nonnull Object changedDataModel,
                                                  @Nonnull String propertyName);

    /**
     * ************************************
     * Bind a view model to a data model so a view reflects the data model's state. The given data model
     * model will be used as the root data model context for the given view model.
     *
     * @param dataModelExecutor The executor that operates on the data model.
     * @param dataModel         The data model to visually represent
     * @param viewModel         The visual representation of the data model
     * @return A future indicating when the operation is done.
     ***************************************
     */
    ListenableFuture<Void> bind(@Nonnull ListeningExecutorService dataModelExecutor,
                                @Nonnull Object dataModel,
                                @Nonnull Object viewModel);

    /**
     * Update a bound view so that the changed sub view is reflected.
     *
     * @param dataModelExecutor The executor that operates on the data model.
     * @param changedViewModel  The view model who's sub view changed.
     * @param subViewName       The name of the sub view that changed.
     * @return  future indicating when the operation is done.
     */
    ListenableFuture<Void> updateViewModelBinding(@Nonnull ListeningExecutorService dataModelExecutor,
                                                  @Nonnull Object changedViewModel,
                                                  @Nonnull String subViewName);
}
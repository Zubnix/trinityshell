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

import javax.annotation.Nonnull;

import com.google.common.util.concurrent.ListeningExecutorService;

/***************************************
 * Binds views to models.
 *
 ***************************************
 */
public interface Binder {

	/***************************************
	 * Update the bound views so they reflect the state of the changed model.
	 *
	 * @param changedViewModel
	 *            The view model that changed.
	 * @param propertyName
	 *            The name of the property that changed.
	 ***************************************
	 */
	void updateBinding(	@Nonnull ListeningExecutorService modelExecutor,
						@Nonnull Object changedViewModel,
						@Nonnull String propertyName);

	/***************************************
	 * Bind a view to a model so it reflects the model's state. The given view
	 * model will be the data context of the given view.
	 *
	 * @param viewModel
	 *            The model to visually represent
	 * @param view
	 *            The visual representation of the model
	 ***************************************
	 */
	void bind(	@Nonnull ListeningExecutorService modelExecutor,
				@Nonnull Object viewModel,
				@Nonnull Object view);

	// TODO
	// void unbind(Object view);
}

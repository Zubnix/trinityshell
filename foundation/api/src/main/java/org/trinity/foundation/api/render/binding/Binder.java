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
package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.shared.OwnerThread;

/***************************************
 * Binds views to models.
 * 
 *************************************** 
 */
@OwnerThread("Render")
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
	void updateBinding(	Object changedViewModel,
						String propertyName);

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
	void bind(	Object viewModel,
				Object view);

	// TODO
	// void unbind(Object view);
}
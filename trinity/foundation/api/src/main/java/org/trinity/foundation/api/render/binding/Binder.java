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

import java.util.concurrent.ExecutionException;

/***************************************
 * Binds views to models and keeps them in sync.
 * 
 *************************************** 
 */
public interface Binder {

	/***************************************
	 * Updates the bound views to reflect the state of the changed model.
	 * 
	 * @param changedModel
	 *            The model that changed.
	 * @param propertyName
	 *            The name of the property that changed.
	 * @throws ExecutionException
	 *             Thrown if illegal model or property is encountered.
	 *************************************** 
	 */
	void updateBinding(	Object changedModel,
						String propertyName) throws ExecutionException;

	/***************************************
	 * Bind a view to a model so it reflects the model's state.
	 * 
	 * @param model
	 *            The model to visually represent
	 * @param view
	 *            The visual representation of the model
	 * @throws ExecutionException
	 *             Thrown if illegal model or property is encountered.
	 *************************************** 
	 */
	void bind(	Object model,
				Object view) throws ExecutionException;

}

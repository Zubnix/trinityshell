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
package org.trinity.foundation.api.render;

import org.trinity.foundation.api.shared.Listenable;

/***************************************
 * Creates new {@link Painter} delegates for a view model.
 * 
 *************************************** 
 */
public interface PainterFactory {

	/**
	 * Creates a new {@code Painter} for the given view model. Multiple calls
	 * with the same model should create a new {@code Painter} instance each
	 * time.
	 * 
	 * @param viewModel
	 *            The view model that the created {@code Painter} will use as a
	 *            visual base.
	 * @return a new {@link Painter}
	 */
	Painter createPainter(final Listenable viewModel);
}

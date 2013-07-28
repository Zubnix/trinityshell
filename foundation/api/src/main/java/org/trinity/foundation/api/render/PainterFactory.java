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
package org.trinity.foundation.api.render;

import org.trinity.foundation.api.shared.AsyncListenable;

/***************************************
 * Creates new {@link Painter} delegates for a view model.
 *
 ***************************************
 */
public interface PainterFactory {

	// FIXME this should return a future. painter creation should be done on the
	// Render thread.
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
	Painter createPainter(final AsyncListenable viewModel);
}

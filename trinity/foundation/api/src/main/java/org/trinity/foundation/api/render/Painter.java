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

import org.trinity.foundation.api.display.DisplayAreaManipulator;
import org.trinity.foundation.api.display.DisplaySurface;

import com.google.common.base.Optional;

/****************************************
 * The gatekeeper to the underlying paint back-end. It talks to a paint back-end
 * by feeding it {@link PaintRoutine}s that will be processed by the paint
 * back-end. It thus provides the means to manipulate the visual appearance of
 * the <code>PaintableSurfaceNode</code> it is bound to.
 * 
 *************************************** 
 */
public interface Painter extends DisplayAreaManipulator {

	/**
	 * 
	 * Get the {@link DisplaySurface} that the painter uses to paint the view.
	 * 
	 * @return A {@link DisplaySurface}.
	 */
	Optional<DisplaySurface> getDislaySurface();

	void bindView();
}

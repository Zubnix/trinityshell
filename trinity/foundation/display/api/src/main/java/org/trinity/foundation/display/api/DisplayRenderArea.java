/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.display.api;

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.foundation.shared.geometry.api.Rectangle;

// TODO documentation
/**
 * A <code>PlatformRenderArea</code> is represents a native isolated graphical
 * area created by a <code>Display</code>. Usually a
 * <code>PlatformRenderArea</code> manages a native window from a native
 * display.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface DisplayRenderArea extends DisplayResource, Area,
		AreaManipulator<DisplayRenderArea>, DisplayEventSource {

	/**
	 * Get the current <code>PlatformRenderArea</code> geometry information. The
	 * returned <code>PlatformRenderAreaGeometry</code> represents this
	 * <code>PlatformRenderArea</code> native geometry at the time of the call.
	 * 
	 * @return This <code>PlatformRenderArea</code>'s
	 *         {@link PlatformRenderAreaGeometry}.
	 */
	Rectangle getGeometry();

	Coordinate getPointerCoordinate();
}

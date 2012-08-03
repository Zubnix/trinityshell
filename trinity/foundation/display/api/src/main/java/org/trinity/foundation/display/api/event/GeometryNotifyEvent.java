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
package org.trinity.foundation.display.api.event;

import org.trinity.foundation.shared.geometry.api.Rectangle;

// TODO is this interface implemented in the fusion-x11 library? If not, it
// should.
/**
 * A <code>ConfigureNotifyEvent</code> notifies that the configuration of a
 * display resource has changed.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class GeometryNotifyEvent extends DisplayEvent {

	private final Rectangle geometry;

	public GeometryNotifyEvent(	final DisplayEventSource displayEventSource,
								final Rectangle geometry) {

		super(displayEventSource);
		this.geometry = geometry;
	}

	/*****************************************
	 * @return the geometry
	 ****************************************/
	public Rectangle getGeometry() {
		return this.geometry;
	}
}
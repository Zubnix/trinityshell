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
package org.trinity.foundation.api.display.event;

import org.trinity.foundation.api.shared.Rectangle;

/**
 * Notifies that the geometry (size, place) of a display resource has changed.
 * 
 */
public class GeometryNotifyEvent extends DisplayEvent {

	private final Rectangle geometry;

	/***************************************
	 * Create a new <code>GeometryNotifyEvent</code> that targets the given
	 * display resource object. The new geometry is specified by the
	 * {@link Rectangle} argument.
	 * 
	 * @param displayEventTarget
	 *            The receiver of this event. eg the display resource who's
	 *            geometry has changed.
	 * @param geometry
	 *            The new geometry as a {@link Rectangle}.
	 *************************************** 
	 */
	public GeometryNotifyEvent(	final Object displayEventTarget,
								final Rectangle geometry) {

		super(displayEventTarget);
		this.geometry = geometry;
	}

	/***************************************
	 * The new geometry of the targeted display resource.
	 * 
	 * @return The new geometry as a {@link Rectangle}.
	 *************************************** 
	 */
	public Rectangle getGeometry() {
		return this.geometry;
	}
}
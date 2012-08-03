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

/**
 * A <code>ConfigureRequestEvent</code> is a request from a display resource to
 * handle and set it's configuration as provided in the request.
 * <p>
 * A <code>ConfigureRequestEvent</code> holds all the information needed to
 * perform the configuration of the <code>EventSource</code> that emitted the
 * <code>DisplayEvent</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class GeometryRequestEvent extends DisplayEvent {

	private final Rectangle geometry;
	private final boolean configureX;
	private final boolean configureY;
	private final boolean configureWidth;
	private final boolean configureHeight;

	public GeometryRequestEvent(final DisplayEventSource displayEventSource,
								final Rectangle geometry,
								final boolean configureX,
								final boolean configureY,
								final boolean configureWidth,
								final boolean configureHeight) {
		super(displayEventSource);
		this.geometry = geometry;
		this.configureX = configureX;
		this.configureY = configureY;
		this.configureWidth = configureWidth;
		this.configureHeight = configureHeight;
	}

	public boolean configureX() {
		return this.configureX;
	}

	public boolean configureY() {
		return this.configureY;
	}

	public boolean configureWidth() {
		return this.configureWidth;
	}

	public boolean configureHeight() {
		return this.configureHeight;
	}

	/*****************************************
	 * @return the rectangle
	 ****************************************/
	public Rectangle getGeometry() {
		return this.geometry;
	}
}

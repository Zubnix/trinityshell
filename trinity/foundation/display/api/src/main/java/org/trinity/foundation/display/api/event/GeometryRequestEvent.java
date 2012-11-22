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
 * A request from a {@link DisplayEventSource} for a new geometry.
 * 
 */
public class GeometryRequestEvent extends DisplayEvent {

	private final Rectangle geometry;
	private final boolean configureX;
	private final boolean configureY;
	private final boolean configureWidth;
	private final boolean configureHeight;

	/***************************************
	 * Create a new <code>GeometryRequestEvent</code> that originated from the
	 * given {@link DisplayEventSource}. The requested geometry is specified by
	 * the given {@link Rectangle} together with a set of booleans to indicate
	 * what values of the <code>Rectangle</code> should be interpreted.
	 * 
	 * @param displayEventSource
	 *            A {@link DisplayEventSource}
	 * @param geometry
	 *            A {@link Rectangle}
	 * @param configureX
	 *            Indicates if the X value should be configured.
	 * @param configureY
	 *            Indicates if the Y value should be configured.
	 * @param configureWidth
	 *            Indicates if the width value should be configured.
	 * @param configureHeight
	 *            Indicates if the height value should be configured.
	 *************************************** 
	 */
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

	public Rectangle getGeometry() {
		return this.geometry;
	}
}

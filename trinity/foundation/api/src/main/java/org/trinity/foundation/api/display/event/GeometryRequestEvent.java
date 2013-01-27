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
 * A request from a display resource for a new geometry.
 * 
 */
public class GeometryRequestEvent extends DisplayEvent {

	private final Rectangle geometry;
	private final boolean configureX;
	private final boolean configureY;
	private final boolean configureWidth;
	private final boolean configureHeight;

	/***************************************
	 * Create a new <code>GeometryRequestEvent</code> that targets the given
	 * display resource.. The requested geometry is specified by the given
	 * {@link Rectangle} together with a set of booleans to indicate what values
	 * of the <code>Rectangle</code> should be interpreted.
	 * 
	 * @param displayEventTarget
	 *            The receiver of this event. eg the display resource who's
	 *            geometry should change.
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
	public GeometryRequestEvent(final Object displayEventTarget,
								final Rectangle geometry,
								final boolean configureX,
								final boolean configureY,
								final boolean configureWidth,
								final boolean configureHeight) {
		super(displayEventTarget);
		this.geometry = geometry;
		this.configureX = configureX;
		this.configureY = configureY;
		this.configureWidth = configureWidth;
		this.configureHeight = configureHeight;
	}

	/**
	 * Indicates if the X value of the new geometry, as specific in
	 * {@link #getGeometry()}, should be interpreted.
	 * 
	 * @return
	 */
	public boolean configureX() {
		return this.configureX;
	}

	/**
	 * Indicates if the Y value of the new geometry, as specific in
	 * {@link #getGeometry()}, should be interpreted.
	 * 
	 * @return
	 */
	public boolean configureY() {
		return this.configureY;
	}

	/**
	 * Indicates if the width value of the new geometry, as specific in
	 * {@link #getGeometry()}, should be interpreted.
	 * 
	 * @return
	 */
	public boolean configureWidth() {
		return this.configureWidth;
	}

	/**
	 * Indicates if the height value of the new geometry, as specific in
	 * {@link #getGeometry()}, should be interpreted.
	 * 
	 * @return
	 */
	public boolean configureHeight() {
		return this.configureHeight;
	}

	/**
	 * The requested geometry. Check the related 'configure' methods to see
	 * which values of the new geometry are requested.
	 * 
	 * @return A {@link Rectangle}.
	 */
	public Rectangle getGeometry() {
		return this.geometry;
	}
}

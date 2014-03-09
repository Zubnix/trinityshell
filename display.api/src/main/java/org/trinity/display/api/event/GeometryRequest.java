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
package org.trinity.display.api.event;

import org.trinity.display.api.event.DisplayEvent;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.media.nativewindow.util.Rectangle;
import javax.media.nativewindow.util.RectangleImmutable;

/**
 * A request from a display resource for a new geometry (size, place).
 *
 */
@Immutable
public class GeometryRequest extends DisplayEvent {

	private final RectangleImmutable geometry;
	private final boolean configureX;
	private final boolean configureY;
	private final boolean configureWidth;
	private final boolean configureHeight;

	/***************************************
	 * Create a new <code>GeometryRequest</code> that targets a given display
	 * resource. The requested geometry is specified by the given
	 * {@link Rectangle} together with a set of booleans to indicate what values
	 * of the <code>Rectangle</code> should be interpreted.
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
	public GeometryRequest(@Nonnull final Rectangle geometry,
							final boolean configureX,
							final boolean configureY,
							final boolean configureWidth,
							final boolean configureHeight) {
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
	 * @return true if the X value should be configured, false if not.
	 */
	public boolean configureX() {
		return this.configureX;
	}

	/**
	 * Indicates if the Y value of the new geometry, as specific in
	 * {@link #getGeometry()}, should be interpreted.
	 *
	 * @return true if the Y value should be configured, false if not.
	 */
	public boolean configureY() {
		return this.configureY;
	}

	/**
	 * Indicates if the width value of the new geometry, as specific in
	 * {@link #getGeometry()}, should be interpreted.
	 *
	 * @return true if the width value should be configured, false if not.
	 */
	public boolean configureWidth() {
		return this.configureWidth;
	}

	/**
	 * Indicates if the height value of the new geometry, as specific in
	 * {@link #getGeometry()}, should be interpreted.
	 *
	 * @return true if the height value should be configured, false if not.
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
	public RectangleImmutable getGeometry() {
		return this.geometry;
	}
}

/*
 * This file is part of Fusion-X11.
 * 
 * Fusion-X11 is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Fusion-X11 is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Fusion-X11. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fusion.x11.core;

import org.trinity.core.display.api.PlatformRenderAreaGeometry;

/**
 * An <code>XWindowGeometry</code> groups the geometric information at a certain
 * point in time from a native X window. More precisely it groups the geometric
 * information at the time of the call to
 * {@link XWindow#getPlatformRenderAreaGeometry()}.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XWindowGeometry implements PlatformRenderAreaGeometry {
	private final int relativeX, relativeY, width, height, borderWidth;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param borderWidth
	 */
	public XWindowGeometry(final int relativeX, final int relativeY,
			final int width, final int height, final int borderWidth) {
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.width = width;
		this.height = height;
		this.borderWidth = borderWidth;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * 
	 * @return
	 */
	public int getBorderWidth() {
		return this.borderWidth;
	}

	@Override
	public int getRelativeY() {
		return this.relativeY;
	}

	@Override
	public int getRelativeX() {
		return this.relativeX;
	}
}

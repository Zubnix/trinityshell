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
package org.fusion.x11.ewmh;

import java.util.Arrays;

import org.trinity.core.display.impl.BasePropertyInstance;
import org.trinity.display.x11.impl.XServerImpl;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetDesktopViewPortInstance extends BasePropertyInstance {

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public static final class DesktopViewPortCoordinate {
		private final int x;
		private final int y;

		public DesktopViewPortCoordinate(final int x, final int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return this.x;
		}

		public int getY() {
			return this.y;
		}
	}

	private final DesktopViewPortCoordinate[] coordinates;

	/**
	 * 
	 * @param display
	 * @param coordinates
	 */
	public _NetDesktopViewPortInstance(final XServerImpl display,
			final DesktopViewPortCoordinate... coordinates) {
		super(display.getxCoreAtoms().getCardinal());
		this.coordinates = coordinates;
	}

	/**
	 * 
	 * @return
	 */
	public DesktopViewPortCoordinate[] getCoordinates() {
		// return a copy so manipulation of the returned instance can take
		// place without interfering with the source.
		return Arrays.copyOf(coordinates, coordinates.length);
	}
}

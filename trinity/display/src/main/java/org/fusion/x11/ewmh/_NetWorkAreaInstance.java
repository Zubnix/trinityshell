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

import org.fusion.x11.core.XDisplay;
import org.trinity.core.display.impl.BasePropertyInstance;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetWorkAreaInstance extends BasePropertyInstance {

	// TODO documentation
	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public static class WorkAreaGeometry {
		private final int x;
		private final int y;
		private final int width;
		private final int height;

		/**
		 * 
		 * @param x
		 * @param y
		 * @param width
		 * @param height
		 */
		public WorkAreaGeometry(final int x, final int y, final int width,
				final int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		/**
		 * 
		 * @return
		 */
		public int getX() {
			return this.x;
		}

		/**
		 * 
		 * @return
		 */
		public int getY() {
			return this.y;
		}

		/**
		 * 
		 * @return
		 */
		public int getWidth() {
			return this.width;
		}

		/**
		 * 
		 * @return
		 */
		public int getHeight() {
			return this.height;
		}
	}

	private final WorkAreaGeometry[] workAreaGeometries;

	/**
	 * 
	 * @param display
	 * @param workAreaGeometries
	 */
	public _NetWorkAreaInstance(final XDisplay display,
			final WorkAreaGeometry... workAreaGeometries) {
		super(display.getxCoreAtoms().getCardinal());
		this.workAreaGeometries = workAreaGeometries;
	}

	/**
	 * 
	 * @return
	 */
	public WorkAreaGeometry[] getWorkAreaGeometries() {
		// return a copy so manipulation of the returned instance can take
		// place without interfering with the source.
		return Arrays.copyOf(this.workAreaGeometries,
				this.workAreaGeometries.length);
	}
}
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
import org.hydrogen.display.api.base.BasePropertyInstance;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetWmIconInstance extends BasePropertyInstance {

	/**
	 * 
	 * @author Erik De Rijcke
	 * @since 1.0
	 */
	public static final class WmIcon {
		private final int width, height;
		private final byte[] argbPixels;

		/**
		 * 
		 * @param width
		 * @param height
		 * @param argbPixels
		 */
		WmIcon(final int width, final int height, final byte[] argbPixels) {
			this.width = width;
			this.height = height;
			this.argbPixels = Arrays.copyOf(argbPixels, argbPixels.length);
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

		/**
		 * 
		 * @return
		 */
		public byte[] getArgbPixels() {
			// return a copy so manipulation of the returned instance can take
			// place without interfering with the source.
			return Arrays.copyOf(this.argbPixels, this.argbPixels.length);
		}
	}

	private final WmIcon[] wmIcons;

	/**
	 * 
	 * @param display
	 * @param wmIcons
	 */
	_NetWmIconInstance(final XDisplay display, final WmIcon[] wmIcons) {
		super(display.getxCoreAtoms().getCardinal());
		this.wmIcons = Arrays.copyOf(wmIcons, wmIcons.length);
	}

	/**
	 * 
	 * @return
	 */
	public WmIcon[] getWmIcons() {
		// return a copy so manipulation of the returned instance can take
		// place without interfering with the source.
		return Arrays.copyOf(this.wmIcons, this.wmIcons.length);
	}

}

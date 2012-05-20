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
package org.fusion.x11.icccm;

import org.fusion.x11.core.XDisplay;
import org.hydrogen.display.api.base.BasePropertyInstance;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class WmIconSizeInstance extends BasePropertyInstance {

	private final int maxWidth;
	private final int maxHeight;
	private final int widthInc;
	private final int heightInc;

	/**
	 * 
	 * @param display
	 * @param maxWidth
	 * @param maxHeight
	 * @param widthInc
	 * @param heightInc
	 */
	public WmIconSizeInstance(final XDisplay display, final int maxWidth,
			final int maxHeight, final int widthInc, final int heightInc) {
		super(display.getDisplayAtoms().getAtomByName("WM_ICON_SIZE"));
		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
		this.heightInc = heightInc;
		this.widthInc = widthInc;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxHeight() {
		return this.maxHeight;
	}

	/**
	 * 
	 * @return
	 */
	public int getHeightInc() {
		return this.heightInc;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxWidth() {
		return this.maxWidth;
	}

	/**
	 * 
	 * @return
	 */
	public int getWidthInc() {
		return this.widthInc;
	}
}

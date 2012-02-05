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

import org.hydrogen.displayinterface.SizePreferences;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XWindowSizePlacePreferences implements SizePreferences {

	private int minWidth;
	private int minHeight;
	private int maxWidth;
	private int maxHeight;
	private int widthInc;
	private int heightInc;
	private int iconX;
	private int iconY;

	/**
	 * 
	 */
	public XWindowSizePlacePreferences() {
		setMinWidth(5);
		setMinHeight(5);
		setMaxWidth(Short.MAX_VALUE);
		setMaxHeight(Short.MAX_VALUE);
		setWidthInc(1);
		setHeightInc(1);
	}

	/**
	 * 
	 * @param heightInc
	 */
	public void setHeightInc(final int heightInc) {
		if (isSaneSize(heightInc)) {
			this.heightInc = heightInc;
		}
	}

	/**
	 * 
	 * @param size
	 * @return
	 */
	protected boolean isSaneSize(final int size) {
		return ((size > 0) && (size <= Short.MAX_VALUE));
	}

	protected boolean isSanePosition(final int position) {
		return ((position > Integer.MIN_VALUE) && (position < Integer.MAX_VALUE));
	}

	/**
	 * 
	 * @param maxHeight
	 */
	public void setMaxHeight(final int maxHeight) {
		if (isSaneSize(maxHeight)) {
			this.maxHeight = maxHeight;
		}
	}

	/**
	 * 
	 * @param maxWidth
	 */
	public void setMaxWidth(final int maxWidth) {
		if (isSaneSize(maxWidth)) {
			this.maxWidth = maxWidth;
		}
	}

	/**
	 * 
	 * @param minHeight
	 */
	public void setMinHeight(final int minHeight) {
		if (isSaneSize(minHeight)) {
			this.minHeight = minHeight;
		}
	}

	/**
	 * 
	 * @param minWidth
	 */
	public void setMinWidth(final int minWidth) {
		if (isSaneSize(minWidth)) {
			this.minWidth = minWidth;
		}
	}

	/**
	 * 
	 * @param widthInc
	 */
	public void setWidthInc(final int widthInc) {
		if (isSaneSize(widthInc)) {
			this.widthInc = widthInc;
		}
	}

	@Override
	public int getMinWidth() {
		return this.minWidth;
	}

	@Override
	public int getMinHeight() {
		return this.minHeight;
	}

	@Override
	public int getMaxWidth() {
		return this.maxWidth;
	}

	@Override
	public int getMaxHeight() {
		return this.maxHeight;
	}

	@Override
	public int getWidthInc() {
		return this.widthInc;
	}

	@Override
	public int getHeightInc() {
		return this.heightInc;
	}

	/**
	 * 
	 * @param iconX
	 */
	public void setIconX(final int iconX) {
		if (isSanePosition(iconX)) {
			this.iconX = iconX;
		}
	}

	/**
	 * 
	 * @param iconY
	 */
	public void setIconY(final int iconY) {
		if (isSanePosition(iconY)) {
			this.iconY = iconY;
		}
	}

	@Override
	public int getIconX() {
		return this.iconX;
	}

	@Override
	public int getIconY() {
		return this.iconY;
	}

}

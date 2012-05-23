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
import org.trinity.core.display.impl.BasePropertyInstance;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class WmSizeHintsInstance extends BasePropertyInstance {
	private final long flags;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private final int minWidth;
	private final int minHeight;
	private final int maxWidth;
	private final int maxHeight;
	private final int widthInc;
	private final int heightInc;
	private final int minAspectNum;
	private final int minAspectDen;
	private final int maxAspectNum;
	private final int maxAspectDen;
	private final int baseWidth;
	private final int baseHeight;
	private final long winGravity;

	/**
	 * 
	 * @param display
	 * @param flags
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param minWidth
	 * @param minHeight
	 * @param maxWidth
	 * @param maxHeight
	 * @param widthInc
	 * @param heightInc
	 * @param minAspectNum
	 * @param minAspectDen
	 * @param maxAspectNum
	 * @param maxAspectDen
	 * @param baseWidth
	 * @param baseHeight
	 * @param winGravity
	 */
	WmSizeHintsInstance(final XDisplay display, final long flags, final int x,
			final int y, final int width, final int height, final int minWidth,
			final int minHeight, final int maxWidth, final int maxHeight,
			final int widthInc, final int heightInc, final int minAspectNum,
			final int minAspectDen, final int maxAspectNum,
			final int maxAspectDen, final int baseWidth, final int baseHeight,
			final long winGravity) {
		super(display.getDisplayAtoms().getAtomByName("WM_SIZE_HINTS"));
		// /** User specified flags */
		// uint32_t flags;
		// /** User-specified position */
		// int32_t x, y;
		// /** User-specified size */
		// int32_t width, height;
		// /** Program-specified minimum size */
		// int32_t min_width, min_height;
		// /** Program-specified maximum size */
		// int32_t max_width, max_height;
		// /** Program-specified resize increments */
		// int32_t width_inc, height_inc;
		// /** Program-specified minimum aspect ratios */
		// int32_t min_aspect_num, min_aspect_den;
		// /** Program-specified maximum aspect ratios */
		// int32_t max_aspect_num, max_aspect_den;
		// /** Program-specified base size */
		// int32_t base_width, base_height;
		// /** Program-specified window gravity */
		// uint32_t win_gravity;
		this.flags = flags;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.widthInc = widthInc;
		this.heightInc = heightInc;
		this.minAspectNum = minAspectNum;
		this.minAspectDen = maxAspectDen;
		this.maxAspectNum = maxAspectNum;
		this.maxAspectDen = maxAspectDen;
		this.baseWidth = baseWidth;
		this.baseHeight = baseHeight;
		this.winGravity = winGravity;
	}

	/**
	 * 
	 * @return
	 */
	public int getBaseHeight() {
		return this.baseHeight;
	}

	/**
	 * 
	 * @return
	 */
	public int getBaseWidth() {
		return this.baseWidth;
	}

	/**
	 * 
	 * @return
	 */
	public long getFlags() {
		return this.flags;
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
	public int getHeightInc() {
		return this.heightInc;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxAspectDen() {
		return this.maxAspectDen;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxAspectNum() {
		return this.maxAspectNum;
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
	public int getMaxWidth() {
		return this.maxWidth;
	}

	/**
	 * 
	 * @return
	 */
	public int getMinAspectDen() {
		return this.minAspectDen;
	}

	/**
	 * 
	 * @return
	 */
	public int getMinAspectNum() {
		return this.minAspectNum;
	}

	/**
	 * 
	 * @return
	 */
	public int getMinHeight() {
		return this.minHeight;
	}

	/**
	 * 
	 * @return
	 */
	public int getMinWidth() {
		return this.minWidth;
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
	public int getWidthInc() {
		return this.widthInc;
	}

	/**
	 * 
	 * @return
	 */
	public long getWinGravity() {
		return this.winGravity;
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
}

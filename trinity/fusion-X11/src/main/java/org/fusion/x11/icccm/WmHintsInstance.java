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
import org.fusion.x11.core.XID;
import org.fusion.x11.core.XWindow;
import org.hydrogen.displayinterface.BasePropertyInstance;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class WmHintsInstance extends BasePropertyInstance {
	private final int flags;
	private final long input;
	private final WmStateEnum initialState;
	private final XID iconPixmap;
	private final XWindow iconWindow;
	private final int iconX;
	private final int iconY;
	private final XID iconMask;
	private final XWindow windowGroup;

	/**
	 * 
	 * @param flags
	 * @param input
	 * @param initialState
	 * @param iconPixmap
	 * @param iconWindow
	 * @param iconX
	 * @param iconY
	 * @param iconMask
	 * @param windowGroup
	 */
	WmHintsInstance(final XDisplay display, final int flags, final long input,
			final WmStateEnum initialState, final XID iconPixmap,
			final XWindow iconWindow, final int iconX, final int iconY,
			final XID iconMask, final XWindow windowGroup) {
		super(display.getDisplayAtoms().getAtomByName("WM_HINTS"));

		// /** Marks which fields in this structure are defined */
		// int32_t flags;
		// /** Does this application rely on the window manager to get keyboard
		// input? */
		// uint32_t input;
		// /** See below */
		// int32_t initial_state;
		// /** Pixmap to be used as icon */
		// xcb_pixmap_t icon_pixmap;
		// /** Window to be used as icon */
		// xcb_window_t icon_window;
		// /** Initial position of icon */
		// int32_t icon_x, icon_y;
		// /** Icon mask bitmap */
		// xcb_pixmap_t icon_mask;
		// /* Identifier of related window group */
		// xcb_window_t window_group;

		this.flags = flags;
		this.input = input;
		this.initialState = initialState;
		this.iconPixmap = iconPixmap;
		this.iconWindow = iconWindow;
		this.iconX = iconX;
		this.iconY = iconY;
		this.iconMask = iconMask;
		this.windowGroup = windowGroup;
	}

	/**
	 * 
	 * @return
	 */
	public int getFlags() {
		return this.flags;
	}

	/**
	 * 
	 * @return
	 */
	public XID getIconMask() {
		return this.iconMask;
	}

	/**
	 * 
	 * @return
	 */
	public XID getIconPixmap() {
		return this.iconPixmap;
	}

	/**
	 * 
	 * @return
	 */
	public XWindow getIconWindow() {
		return this.iconWindow;
	}

	/**
	 * 
	 * @return
	 */
	public int getIconX() {
		return this.iconX;
	}

	/**
	 * 
	 * @return
	 */
	public int getIconY() {
		return this.iconY;
	}

	/**
	 * 
	 * @return
	 */
	public WmStateEnum getInitialState() {
		return this.initialState;
	}

	/**
	 * 
	 * @return
	 */
	public long getInput() {
		return this.input;
	}

	/**
	 * 
	 * @return
	 */
	public XWindow getWindowGroup() {
		return this.windowGroup;
	}
}

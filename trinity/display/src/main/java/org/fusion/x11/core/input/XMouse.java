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
package org.fusion.x11.core.input;

import org.fusion.x11.core.XCoreInterface;
import org.fusion.x11.core.XDisplay;
import org.trinity.core.input.api.Mouse;

/**
 * An <code>XMouse</code> represents an X mouse pointer on an X display server.
 * It holds the location of an X mouse pointer relative to the root window.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class XMouse implements Mouse {

	private final XDisplay display;
	private final XCoreInterface displayInterface;
	private int rootX;
	private int rootY;

	/**
	 * Create a new <code>XMouse</code> with the given <code>XDisplay</code> and
	 * with the given <code>XCoreInterface</code>.
	 * <p>
	 * The given <code>XDisplay</code> will represent the native X display where
	 * this <code>XMouse</code> will live on.
	 * <p>
	 * The given <code>XCoreInterface</code> will be used to talk to the native
	 * X back-end.
	 * 
	 * @param display
	 *            An {@link XDisplay}.
	 * @param displayInterface
	 *            An {@link XCoreInterface}.
	 */
	public XMouse(final XDisplay display, final XCoreInterface displayInterface) {
		this.displayInterface = displayInterface;
		this.display = display;
		refreshInfo();
	}

	/**
	 * The <code>XDisplay</code> where this <code>XMouse</code> lives on.
	 * 
	 * @return An {@link XDisplay}.
	 */
	public XDisplay getDisplay() {
		return this.display;
	}

	@Override
	public int getRootX() {
		final int returnint = this.rootX;
		return returnint;
	}

	@Override
	public int getRootY() {
		final int returnint = this.rootY;
		return returnint;
	}

	@Override
	public void refreshInfo() {
		this.displayInterface.updateXMousePointer(this);
	}

	/**
	 * Store the horizontal position of this <code>XMouse</code> relative to the
	 * root window.
	 * 
	 * @param rootX
	 *            The X coordinate, in pixels.
	 */
	public void updateRootX(final int rootX) {
		this.rootX = rootX;
	}

	/**
	 * Store the vertical position of this <code>XMouse</code> relative to the
	 * root window.
	 * 
	 * @param rootY
	 *            The Y coordinate, in pixels.
	 */
	public void updateRootY(final int rootY) {
		this.rootY = rootY;
	}
}

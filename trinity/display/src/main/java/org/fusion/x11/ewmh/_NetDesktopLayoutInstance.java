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

import org.trinity.core.display.impl.BasePropertyInstance;
import org.trinity.display.x11.impl.XServerImpl;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetDesktopLayoutInstance extends BasePropertyInstance {

	private final DesktopLayoutOrientation orientation;
	private final int columns;
	private final int rows;
	private final DesktopLayoutStartingPoint startingCorner;

	/**
	 * 
	 * @param display
	 * @param orientation
	 * @param columns
	 * @param rows
	 * @param startingCorner
	 */
	_NetDesktopLayoutInstance(final XServerImpl display,
			final DesktopLayoutOrientation orientation, final int columns,
			final int rows, final DesktopLayoutStartingPoint startingCorner) {
		super(display.getxCoreAtoms().getCardinal());
		this.orientation = orientation;
		this.columns = columns;
		this.rows = rows;
		this.startingCorner = startingCorner;
	}

	/**
	 * 
	 * @return
	 */
	public int getColumns() {
		return this.columns;
	}

	/**
	 * 
	 * @return
	 */
	public int getRows() {
		return this.rows;
	}

	/**
	 * 
	 * @return
	 */
	public DesktopLayoutStartingPoint getStartingCorner() {
		return this.startingCorner;
	}

	/**
	 * 
	 * @return
	 */
	public DesktopLayoutOrientation getOrientation() {
		return this.orientation;
	}

}

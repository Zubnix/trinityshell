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

import org.trinity.display.x11.impl.XServerImpl;

//TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class Icccm {
	public static final int MAJOR = 2;
	public static final int MINOR = 0;

	private final IcccmAtoms icccmAtoms;
	private final SelectionManager selectionManager;
	private final XServerImpl display;

	/**
	 * 
	 * @param display
	 */
	public Icccm(final XServerImpl display) {
		this.display = display;
		this.icccmAtoms = new IcccmAtoms(display);
		this.selectionManager = new SelectionManager(getIcccmAtoms());
	}

	/**
	 * 
	 * @return
	 */
	public XServerImpl getDisplay() {
		return this.display;
	}

	/**
	 * 
	 * @return
	 */
	public IcccmAtoms getIcccmAtoms() {
		return this.icccmAtoms;
	}

	/**
	 * 
	 * @return
	 */
	public SelectionManager getSelectionManager() {
		return this.selectionManager;
	}
}

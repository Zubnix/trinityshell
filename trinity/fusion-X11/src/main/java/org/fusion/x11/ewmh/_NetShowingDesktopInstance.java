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

import org.fusion.x11.core.XDisplay;
import org.trinity.core.display.impl.BasePropertyInstance;

// TODO documentation
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public final class _NetShowingDesktopInstance extends BasePropertyInstance {

	private final boolean showingDesktop;

	/**
	 * 
	 * @param display
	 * @param showingDesktop
	 */
	public _NetShowingDesktopInstance(final XDisplay display,
			final boolean showingDesktop) {
		super(display.getxCoreAtoms().getCardinal());
		this.showingDesktop = showingDesktop;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isShowingDesktop() {
		return this.showingDesktop;
	}
}
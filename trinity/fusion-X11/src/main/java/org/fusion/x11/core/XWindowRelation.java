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

import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.PlatformRenderAreaRelation;

// TODO documentation
/**
 * An <code>XWindowRelation</code> defines the relation between 2
 * <code>XWindow</code>s. For example an <code>XWindow</code> that functions as
 * a pop-up for another <code>XWindow</code> will have an
 * <code>XWindowRelation</code> to this <code>XWindow</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class XWindowRelation implements PlatformRenderAreaRelation {
	private final XWindow relatedRenderArea;

	/**
	 * 
	 * @param relatedRenderArea
	 */
	public XWindowRelation(final XWindow relatedRenderArea) {
		this.relatedRenderArea = relatedRenderArea;
	}

	@Override
	public PlatformRenderArea getRelatedRenderArea() {
		return this.relatedRenderArea;
	}
}
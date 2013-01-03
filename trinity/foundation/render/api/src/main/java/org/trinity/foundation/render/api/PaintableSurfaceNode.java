/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.render.api;

/***************************************
 * A {@link SurfaceNode} who's visual appearance can be modified.
 * 
 *************************************** 
 */
public interface PaintableSurfaceNode extends SurfaceNode {

	/***************************************
	 * The {@link Painter} of this <code>PaintableSurfaceNode</code>.
	 * <p>
	 * A <code>Painter</code> is responsible for the visual manipulation of the
	 * <code>PaintableSurfaceNode</code> it belongs to.
	 * 
	 * 
	 * @return The {@link Painter} that manages this
	 *         <code>PaintableSurfaceNode</code>.
	 *************************************** 
	 */
	Painter getPainter();

	/**
	 * The closest ancestor that is of type <code>PaintableSurfaceNode</code>.
	 * This is not necessarily the direct parent of this
	 * <code>PaintableSurfaceNode</code>.
	 * 
	 * @return The closes ancestor that is a <code>PaintableSurfaceNode</code>.
	 */
	PaintableSurfaceNode getParentPaintableSurface();
}

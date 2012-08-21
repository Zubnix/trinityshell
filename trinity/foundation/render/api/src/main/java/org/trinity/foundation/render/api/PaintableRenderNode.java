/*
 * This file is part of Hydrogen. Hydrogen is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Hydrogen is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with Hydrogen. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.foundation.render.api;

/**
 * A <code>PaintableRenderNode</code> extends the <code>HierarchicalArea</code>
 * by providing a <code>Painter</code> as a way to manipulate its visual
 * state.It is a rectangular area that can be visually manipulated through a
 * <code>Painter</code>.
 * <p>
 * A <code>PaintableRenderNode</code>'s visual representation, called a paint
 * peer, is usually implemented by a separate paint toolkit. It's the
 * <code>Painter</code>'s job to talk to this toolkit through a paint back-end
 * by issueing it <code>PaintCall</code>s.
 * 
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface PaintableRenderNode extends RenderNode {

	/**
	 * The <code>Painter</code> of this <code>PaintableRenderNode</code>.
	 * <p>
	 * A <code>Painter</code> is responsible for the visual manipulation of the
	 * <code>PaintableRenderNode</code> it belongs to.
	 * 
	 * 
	 * @return The {@link Painter} that manages this
	 *         <code>PaintableRenderNode</code>.
	 */
	Painter getPainter();

	/**
	 * The closest ancestor that is of type <code>PaintableRenderNode</code>.
	 * This is not necessarily the direct parent of this
	 * <code>PaintableRenderNode</code>.
	 * 
	 * @return The closes ancestor that is a <code>PaintableRenderNode</code>.
	 */
	PaintableRenderNode getParentPaintableRenderNode();
}

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

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplayAreaManipulator;

/**
 * A <code>Painter</code> can be seen as the "gate keeper" to the underlying
 * paint back-end. It talks to a paint back-end by feeding it
 * <code>PaintCall</code>s.It provides the means to manipulate a
 * <code>PaintableSurfaceNode</code> area.
 * <p>
 * A <code>Painter</code> talks to a paint back-end that provides the visual
 * implementation for a <code>PaintableSurfaceNode</code>. It does this by
 * passing a paint toolkit specific implementation of a <code>PaintCall</code>
 * object to the paint back-end. This implies that a <code>Painter</code>
 * functions as a "bridge" between the model thread and the gui thread.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface Painter extends DisplayAreaManipulator<PaintableSurfaceNode> {

	<R> Future<R> instruct(PaintInstruction<R, ? extends PaintContext> paintInstruction);

	/**
	 * The <code>PaintableSurfaceNode</code> that is managed by this
	 * <code>Painter</code>.
	 * 
	 * @return
	 */
	PaintableSurfaceNode getPaintableRenderNode();
}

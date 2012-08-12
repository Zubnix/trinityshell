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

import org.trinity.foundation.display.api.AreaManipulator;
import org.trinity.foundation.display.api.ResourceHandle;

/**
 * A <code>Painter</code> can be seen as the "gate keeper" to the underlying
 * paint back-end. It talks to a paint back-end by feeding it
 * <code>PaintCall</code>s.It provides the means to manipulate a
 * <code>Paintable</code> area.
 * <p>
 * A <code>Painter</code> talks to a paint back-end that provides the visual
 * implementation for a <code>Paintable</code>. It does this by passing a paint
 * toolkit specific implementation of a <code>PaintCall</code> object to the
 * paint back-end. This implies that a <code>Painter</code> functions as a
 * "bridge" between the model thread and the gui thread.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public interface Painter extends AreaManipulator<Paintable> {

	// TODO recycle javadoc on website's documentation
	/**
	 * Create a new paint peer and get to the native render area id of this
	 * paint peer. This id is a handle to the native render area where the
	 * <code>Paintable</code>'s visual will be drawn on. This native render area
	 * is not manipulated directly but is used by the paint back-end to draw in.
	 * <p>
	 * This method does not necessarily create a new native render area id as
	 * multiple <code>Paintable</code>s can be drawn on the same native render
	 * area. Instead the id of the shared native render area can be returned
	 * depending on the paint toolkit and the given <code>PaintCall</code>
	 * implementation.
	 * <p>
	 * Implementation advice for the paint back-end:
	 * <p>
	 * The given <code>PaintCall</code> should create a new paint peer and
	 * return this paint peer when finished. It is then the paint back-end's job
	 * to extract a native render area id from this paint peer. The provided
	 * parentPaintable argument should be used by the paint back-end to look up
	 * the corresponding parent paint peer. This parent paint peer should then
	 * be given as the first argument when executing the <code>PaintCall</code>.
	 * The second argument should be the <code>Paintable</code> that this
	 * <code>Painter</code> manages. The given <code>PaintCall</code> should be
	 * executed synchroniously (blocking) by the paint back-end.
	 * 
	 * @param parentPaintable
	 *            The parent <code>Paintable</code> of the
	 *            <code>Paintable</code> that is managed by this
	 *            <code>Painter</code>.
	 * @param paintCall
	 *            The <code>PaintCall</code> that will create a new paint
	 *            toolkit specific paint peer.
	 * @return the id of the underlying native render area. This id can be used
	 *         to get a reference to a native render area.
	 */
	// ResourceHandle initPaintPeer(Paintable parentPaintable,
	// PaintCall<?, ?> paintCall);

	/**
	 * Execute the given <code>PaintCall</code>. The <code>PaintCall</code> is
	 * executed by the paint back-end thread.
	 * <p>
	 * Implementation advice for the paint back-end:
	 * <p>
	 * The first argument with which the <code>PaintCall</code> is executed
	 * should be the paint peer that was created by <code>initPaintPeer</code>.
	 * The second argument should be the <code>Paintable</code> that this
	 * <code>Painter</code> manages. The given <code>PaintCall</code> should be
	 * executed asynchroniously (non blocking) by the paint back-end.
	 * 
	 * @param paintCall
	 *            A <code>PaintCall</code> implementation that will be passed to
	 *            the underlying paint back-end for execution.
	 */
	void instruct(PaintInstruction<? extends RenderEngine> paintInstruction);

	ResourceHandle construct(PaintConstruction<? extends RenderEngine> paintConstruction);

	<R> R calculate(PaintCalculation<R, ? extends RenderEngine> paintCalculation);

	/**
	 * The <code>Paintable</code> that is managed by this <code>Painter</code>.
	 * 
	 * @return
	 */
	Paintable getPaintable();
}

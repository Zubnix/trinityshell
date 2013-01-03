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

import java.util.concurrent.Future;

import org.trinity.foundation.display.api.DisplayAreaManipulator;

/****************************************
 * The gatekeeper to the underlying paint back-end. It talks to a paint back-end
 * by feeding it {@link PaintInstruction}s that will be processed by the paint
 * back-end. It thus provides the means to manipulate the visual appearance of
 * the <code>PaintableSurfaceNode</code> it is bound to.
 * 
 *************************************** 
 */
public interface Painter extends DisplayAreaManipulator {

	/***************************************
	 * Pass a {@link PaintInstruction} to the paint back-end that will be
	 * processed by that paint back-end.
	 * 
	 * @param paintInstruction
	 *            a {@link PaintInstruction}.
	 * @return a {@link Future} that will return the result of the
	 *         <code>PaintInstruction</code>. This feature will block until the
	 *         <code>PaintInstruction</code> is completed.
	 *************************************** 
	 */
	<R> Future<R> instruct(PaintInstruction<R, ? extends PaintContext> paintInstruction);

	/**
	 * The <code>PaintableSurfaceNode</code> that is managed by this
	 * <code>Painter</code>.
	 * 
	 * @return the {@link PaintableSurfaceNode}.
	 */
	PaintableSurfaceNode getPaintableRenderNode();
}

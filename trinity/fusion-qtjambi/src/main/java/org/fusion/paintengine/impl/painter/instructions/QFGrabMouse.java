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
package org.fusion.paintengine.impl.painter.instructions;

import org.fusion.paintengine.api.QFRenderEngine;
import org.fusion.paintengine.api.painter.QFPaintInstruction;
import org.hydrogen.paint.api.Paintable;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class QFGrabMouse implements QFPaintInstruction {

	/*
	 * (non-Javadoc)
	 * @see
	 * org.hydrogen.paint.api.PaintInstruction#call(org.hydrogen.paint.api.Paintable
	 * , org.hydrogen.paint.api.RenderEngine)
	 */
	@Override
	public void call(	final Paintable paintable,
						final QFRenderEngine renderEngine) {
		renderEngine.getVisual(paintable).grabMouse();
	}
}

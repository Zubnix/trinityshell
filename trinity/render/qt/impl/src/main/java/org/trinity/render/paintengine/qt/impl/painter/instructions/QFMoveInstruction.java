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
package org.trinity.render.paintengine.qt.impl.painter.instructions;

import org.trinity.foundation.render.api.Paintable;
import org.trinity.render.paintengine.qt.api.QFRenderEngine;
import org.trinity.render.paintengine.qt.api.painter.QFPaintInstruction;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/*****************************************
 * @author Erik De Rijcke
 ****************************************/
public class QFMoveInstruction implements QFPaintInstruction {

	private final int x;
	private final int y;

	@Inject
	protected QFMoveInstruction(@Assisted("x") final int x,
								@Assisted("y") final int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void call(	final Paintable paintable,
						final QFRenderEngine renderEngine) {
		renderEngine.getVisual(paintable).move(this.x, this.y);
	}
}

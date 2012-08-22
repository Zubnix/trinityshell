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
package org.trinity.render.qt.impl.painter.instructions;

import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.render.paintengine.qt.api.QJPaintContext;

public class QJSetParentInstruction implements
		PaintInstruction<Void, QJPaintContext> {

	private final PaintableRenderNode parent;
	private final int x;
	private final int y;

	public QJSetParentInstruction(	final PaintableRenderNode parent,
									final int x,
									final int y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
	}

	@Override
	public Void call(	final PaintableRenderNode paintableRenderNode,
						final QJPaintContext renderEngine) {
		renderEngine.getVisual()
				.setParent(renderEngine.queryVisual(this.parent));
		renderEngine.getVisual().move(this.x, this.y);
		return null;
	}
}
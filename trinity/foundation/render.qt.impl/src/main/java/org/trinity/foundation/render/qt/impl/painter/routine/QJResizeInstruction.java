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
package org.trinity.foundation.render.qt.impl.painter.routine;

import org.trinity.foundation.api.render.PaintContext;
import org.trinity.foundation.api.render.PaintRoutine;

import com.trolltech.qt.gui.QWidget;

public class QJResizeInstruction implements PaintRoutine<Void, PaintContext> {

	private final QWidget view;
	private final int width;
	private final int height;

	public QJResizeInstruction(final QWidget view, final int width, final int height) {
		this.view = view;
		this.width = width;
		this.height = height;
	}

	@Override
	public Void call(final PaintContext paintContext) {
		this.view.resize(	this.width,
							this.height);
		return null;
	}
}
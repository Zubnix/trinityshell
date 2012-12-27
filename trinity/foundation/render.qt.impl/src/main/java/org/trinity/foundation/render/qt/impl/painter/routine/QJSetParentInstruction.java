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

public class QJSetParentInstruction implements PaintRoutine<Void, PaintContext> {

	private final QWidget view;
	private final QWidget parentView;
	private final int x;
	private final int y;

	public QJSetParentInstruction(final QWidget view, final QWidget parentView, final int x, final int y) {
		this.view = view;
		this.parentView = parentView;
		this.x = x;
		this.y = y;
	}

	@Override
	public Void call(final PaintContext paintContext) {
		this.view.setParent(this.parentView);
		this.view.move(	this.x,
						this.y);
		return null;
	}
}
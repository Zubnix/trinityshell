/*
 * This file is part of Fusion-qtjambi. Fusion-qtjambi is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. Fusion-qtjambi is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with Fusion-qtjambi. If not,
 * see <http://www.gnu.org/licenses/>.
 */
package org.trinity.render.paintengine.qt.impl.painter;

import org.trinity.foundation.display.api.ResourceHandle;
import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.render.api.PaintCalculation;
import org.trinity.foundation.render.api.PaintConstruction;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.Paintable;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.render.api.RenderEngine;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.render.paintengine.qt.api.QFRenderEngine;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFDestroyInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFGiveFocusInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFGrabKeyboardInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFGrabPointer;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFHideInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFLowerInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFMoveInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFMoveResizeInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFRaiseInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFReleaseKeyboardInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFReleaseMouseInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFResizeInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFSetParentInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFShowInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QFTranslateCoordinatesCalculation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class QFPainter implements Painter {

	private final PaintInstruction<QFRenderEngine> lower = new QFLowerInstruction();
	private final PaintInstruction<QFRenderEngine> raise = new QFRaiseInstruction();
	private final PaintInstruction<QFRenderEngine> hide = new QFHideInstruction();
	private final PaintInstruction<QFRenderEngine> show = new QFShowInstruction();
	private final PaintInstruction<QFRenderEngine> grabKeyboard = new QFGrabKeyboardInstruction();
	private final PaintInstruction<QFRenderEngine> releaseKeyboard = new QFReleaseKeyboardInstruction();
	private final PaintInstruction<QFRenderEngine> grabPointer = new QFGrabPointer();
	private final PaintInstruction<QFRenderEngine> releaseMouse = new QFReleaseMouseInstruction();
	private final PaintInstruction<QFRenderEngine> destroy = new QFDestroyInstruction();
	private final PaintInstruction<QFRenderEngine> giveFocus = new QFGiveFocusInstruction();

	private final QFRenderEngine qFRenderEngine;
	private final Paintable paintable;

	@Inject
	protected QFPainter(final QFRenderEngine qFRenderEngine,
						@Assisted final Paintable paintable) {

		this.qFRenderEngine = qFRenderEngine;
		this.paintable = paintable;
	}

	@Override
	public Paintable getPaintable() {
		return this.paintable;
	}

	@Override
	public void destroy() {
		this.qFRenderEngine.invoke(this.paintable, this.destroy);
	}

	@Override
	public void setInputFocus() {
		this.qFRenderEngine.invoke(this.paintable, this.giveFocus);
	}

	@Override
	public void lower() {
		this.qFRenderEngine.invoke(this.paintable, this.lower);
	}

	@Override
	public void show() {
		this.qFRenderEngine.invoke(this.paintable, this.show);
	}

	@Override
	public void move(final int x, final int y) {
		this.qFRenderEngine.invoke(this.paintable, new QFMoveInstruction(x, y));
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		this.qFRenderEngine.invoke(	this.paintable,
									new QFMoveResizeInstruction(x,
																y,
																width,
																height));
	}

	@Override
	public void raise() {
		this.qFRenderEngine.invoke(this.paintable, this.raise);
	}

	@Override
	public void setParent(final Paintable parent, final int x, final int y) {
		this.qFRenderEngine.invoke(	this.paintable,
									new QFSetParentInstruction(parent, x, y));
	}

	@Override
	public void resize(final int width, final int height) {
		this.qFRenderEngine.invoke(	this.paintable,
									new QFResizeInstruction(width, height));
	}

	@Override
	public void hide() {
		this.qFRenderEngine.invoke(this.paintable, this.hide);
	}

	@Override
	public void grabKeyboard() {
		this.qFRenderEngine.invoke(this.paintable, this.grabKeyboard);
	}

	@Override
	public Coordinate translateCoordinates(	final Paintable source,
											final int sourceX,
											final int sourceY) {
		return this.qFRenderEngine
				.invoke(this.paintable,
						new QFTranslateCoordinatesCalculation(	source,
																sourceX,
																sourceY));
	}

	@Override
	public void instruct(final PaintInstruction<? extends RenderEngine> paintInstruction) {
		this.qFRenderEngine.invoke(this.paintable, paintInstruction);
	}

	@Override
	public ResourceHandle construct(final PaintConstruction<? extends RenderEngine> paintConstruction) {
		return this.qFRenderEngine.invoke(this.paintable, paintConstruction);
	}

	@Override
	public <R> R calculate(final PaintCalculation<R, ? extends RenderEngine> paintCalculation) {
		return this.qFRenderEngine.invoke(this.paintable, paintCalculation);
	}

	@Override
	public void grabButton(	final Button grabButton,
							final InputModifiers withModifiers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void grabPointer() {
		this.qFRenderEngine.invoke(this.paintable, this.grabPointer);
	}

	@Override
	public void ungrabPointer() {
		this.qFRenderEngine.invoke(this.paintable, this.releaseMouse);

	}

	@Override
	public void ungrabButton(	final Button ungrabButton,
								final InputModifiers withModifiers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void grabKey(final Key grabKey, final InputModifiers withModifiers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ungrabKey(	final Key ungrabKey,
							final InputModifiers withModifiers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void ungrabKeyboard() {
		this.qFRenderEngine.invoke(this.paintable, this.releaseKeyboard);

	}
}

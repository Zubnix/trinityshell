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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.trinity.foundation.input.api.Button;
import org.trinity.foundation.input.api.InputModifiers;
import org.trinity.foundation.input.api.Key;
import org.trinity.foundation.render.api.PaintContext;
import org.trinity.foundation.render.api.PaintInstruction;
import org.trinity.foundation.render.api.PaintableRenderNode;
import org.trinity.foundation.render.api.Painter;
import org.trinity.foundation.shared.geometry.api.Coordinate;
import org.trinity.render.paintengine.qt.api.QJPaintContext;
import org.trinity.render.paintengine.qt.impl.QJRenderEngine;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJDestroyInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJGiveFocusInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJGrabKeyboardInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJGrabPointer;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJHideInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJLowerInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJMoveInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJMoveResizeInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJRaiseInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJReleaseKeyboardInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJReleaseMouseInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJResizeInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJSetParentInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJShowInstruction;
import org.trinity.render.paintengine.qt.impl.painter.instructions.QJTranslateCoordinatesCalculation;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class QJPainter implements Painter {

	private final PaintInstruction<Void, QJPaintContext> lower = new QJLowerInstruction();
	private final PaintInstruction<Void, QJPaintContext> raise = new QJRaiseInstruction();
	private final PaintInstruction<Void, QJPaintContext> hide = new QJHideInstruction();
	private final PaintInstruction<Void, QJPaintContext> show = new QJShowInstruction();
	private final PaintInstruction<Void, QJPaintContext> grabKeyboard = new QJGrabKeyboardInstruction();
	private final PaintInstruction<Void, QJPaintContext> releaseKeyboard = new QJReleaseKeyboardInstruction();
	private final PaintInstruction<Void, QJPaintContext> grabPointer = new QJGrabPointer();
	private final PaintInstruction<Void, QJPaintContext> releaseMouse = new QJReleaseMouseInstruction();
	private final PaintInstruction<Void, QJPaintContext> destroy = new QJDestroyInstruction();
	private final PaintInstruction<Void, QJPaintContext> giveFocus = new QJGiveFocusInstruction();

	private final QJRenderEngine qFRenderEngine;
	private final PaintableRenderNode paintableRenderNode;

	@Inject
	protected QJPainter(final QJRenderEngine qFRenderEngine,
						@Assisted final PaintableRenderNode paintableRenderNode) {

		this.qFRenderEngine = qFRenderEngine;
		this.paintableRenderNode = paintableRenderNode;
	}

	@Override
	public PaintableRenderNode getPaintableRenderNode() {
		return this.paintableRenderNode;
	}

	@Override
	public void destroy() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.destroy);
	}

	@Override
	public void setInputFocus() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.giveFocus);
	}

	@Override
	public void lower() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.lower);
	}

	@Override
	public void show() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.show);
	}

	@Override
	public void move(final int x, final int y) {
		this.qFRenderEngine.invoke(	this.paintableRenderNode,
									new QJMoveInstruction(x, y));
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		this.qFRenderEngine.invoke(	this.paintableRenderNode,
									new QJMoveResizeInstruction(x,
																y,
																width,
																height));
	}

	@Override
	public void raise() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.raise);
	}

	@Override
	public void setParent(	final PaintableRenderNode parent,
							final int x,
							final int y) {
		this.qFRenderEngine.invoke(	this.paintableRenderNode,
									new QJSetParentInstruction(parent, x, y));
	}

	@Override
	public void resize(final int width, final int height) {
		this.qFRenderEngine.invoke(	this.paintableRenderNode,
									new QJResizeInstruction(width, height));
	}

	@Override
	public void hide() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.hide);
	}

	@Override
	public void grabKeyboard() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.grabKeyboard);
	}

	@Override
	public Coordinate translateCoordinates(	final PaintableRenderNode source,
											final int sourceX,
											final int sourceY) {
		try {
			return this.qFRenderEngine
					.invoke(this.paintableRenderNode,
							new QJTranslateCoordinatesCalculation(	source,
																	sourceX,
																	sourceY))
					.get();
		} catch (final InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (final ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R> Future<R> instruct(final PaintInstruction<R, ? extends PaintContext> paintInstruction) {
		return this.qFRenderEngine
				.invoke(this.paintableRenderNode,
						(PaintInstruction<R, QJPaintContext>) paintInstruction);
	}

	@Override
	public void grabButton(	final Button grabButton,
							final InputModifiers withModifiers) {
		// TODO Auto-generated method stub

	}

	@Override
	public void grabPointer() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.grabPointer);
	}

	@Override
	public void ungrabPointer() {
		this.qFRenderEngine.invoke(this.paintableRenderNode, this.releaseMouse);

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
		this.qFRenderEngine.invoke(	this.paintableRenderNode,
									this.releaseKeyboard);

	}
}

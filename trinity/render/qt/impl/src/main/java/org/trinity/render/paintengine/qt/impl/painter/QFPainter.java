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
import org.trinity.render.paintengine.qt.api.QFPaintContext;
import org.trinity.render.paintengine.qt.impl.QFRenderEngineImpl;
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

	private final PaintInstruction<Void, QFPaintContext> lower = new QFLowerInstruction();
	private final PaintInstruction<Void, QFPaintContext> raise = new QFRaiseInstruction();
	private final PaintInstruction<Void, QFPaintContext> hide = new QFHideInstruction();
	private final PaintInstruction<Void, QFPaintContext> show = new QFShowInstruction();
	private final PaintInstruction<Void, QFPaintContext> grabKeyboard = new QFGrabKeyboardInstruction();
	private final PaintInstruction<Void, QFPaintContext> releaseKeyboard = new QFReleaseKeyboardInstruction();
	private final PaintInstruction<Void, QFPaintContext> grabPointer = new QFGrabPointer();
	private final PaintInstruction<Void, QFPaintContext> releaseMouse = new QFReleaseMouseInstruction();
	private final PaintInstruction<Void, QFPaintContext> destroy = new QFDestroyInstruction();
	private final PaintInstruction<Void, QFPaintContext> giveFocus = new QFGiveFocusInstruction();

	private final QFRenderEngineImpl qFRenderEngine;
	private final PaintableRenderNode paintableRenderNode;

	@Inject
	protected QFPainter(final QFRenderEngineImpl qFRenderEngine,
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
									new QFMoveInstruction(x, y));
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		this.qFRenderEngine.invoke(	this.paintableRenderNode,
									new QFMoveResizeInstruction(x,
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
									new QFSetParentInstruction(parent, x, y));
	}

	@Override
	public void resize(final int width, final int height) {
		this.qFRenderEngine.invoke(	this.paintableRenderNode,
									new QFResizeInstruction(width, height));
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
							new QFTranslateCoordinatesCalculation(	source,
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
						(PaintInstruction<R, ? extends QFPaintContext>) paintInstruction);
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

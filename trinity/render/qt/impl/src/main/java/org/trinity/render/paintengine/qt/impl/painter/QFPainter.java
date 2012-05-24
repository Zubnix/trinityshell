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

import javax.inject.Named;

import org.trinity.core.display.api.ResourceHandle;
import org.trinity.core.geometry.api.Coordinates;
import org.trinity.core.render.api.PaintCalculation;
import org.trinity.core.render.api.PaintConstruction;
import org.trinity.core.render.api.PaintInstruction;
import org.trinity.core.render.api.Paintable;
import org.trinity.core.render.api.Painter;
import org.trinity.core.render.api.RenderEngine;
import org.trinity.render.paintengine.qt.api.QFRenderEngine;
import org.trinity.render.paintengine.qt.api.painter.QFPaintCalculationFactory;
import org.trinity.render.paintengine.qt.api.painter.QFPaintConstruction;
import org.trinity.render.paintengine.qt.api.painter.QFPaintInstruction;
import org.trinity.render.paintengine.qt.api.painter.QFPaintInstructionFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class QFPainter implements Painter {

	private final QFPaintInstruction lower;
	private final QFPaintInstruction raise;
	private final QFPaintInstruction hide;
	private final QFPaintInstruction show;
	private final QFPaintInstruction grabKeyboard;
	private final QFPaintInstruction releaseKeyboard;
	private final QFPaintConstruction grabMouse;
	private final QFPaintConstruction releaseMouse;
	private final QFPaintConstruction destroy;
	private final QFPaintConstruction giveFocus;

	private final QFPaintInstructionFactory paintInstructionFactory;
	private final QFPaintCalculationFactory paintCalculationFactory;

	private final QFRenderEngine qFRenderEngine;
	private final Paintable paintable;

	@Inject
	protected QFPainter(@Named("QFDestroy") final QFPaintConstruction destroy,
						@Named("QFGiveFocus") final QFPaintConstruction giveFocus,
						@Named("QFGrabKeyboard") final QFPaintInstruction grabKeyboard,
						@Named("QFGrabMouse") final QFPaintConstruction grabMouse,
						@Named("QFHide") final QFPaintInstruction hide,
						@Named("QFLower") final QFPaintInstruction lower,
						@Named("QFRaise") final QFPaintInstruction raise,
						@Named("QFReleaseKeyboard") final QFPaintInstruction releaseKeyboard,
						@Named("QFReleaseMouse") final QFPaintConstruction releaseMouse,
						@Named("QFShow") final QFPaintInstruction show,

						final QFPaintInstructionFactory paintInstructionFactory,
						final QFPaintCalculationFactory paintCalculationFactory,

						final QFRenderEngine qFRenderEngine,

						@Assisted final Paintable paintable) {
		this.lower = lower;
		this.raise = raise;
		this.hide = hide;
		this.show = show;
		this.grabKeyboard = grabKeyboard;
		this.releaseKeyboard = releaseKeyboard;
		this.grabMouse = grabMouse;
		this.releaseMouse = releaseMouse;
		this.destroy = destroy;
		this.giveFocus = giveFocus;

		this.paintInstructionFactory = paintInstructionFactory;
		this.paintCalculationFactory = paintCalculationFactory;

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
		this.qFRenderEngine.invoke(this.paintable, this.paintInstructionFactory
				.createMoveInstruction(x, y));
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		this.qFRenderEngine.invoke(this.paintable, this.paintInstructionFactory
				.createMoveResizeInstruction(x, y, width, height));
	}

	@Override
	public void raise() {
		this.qFRenderEngine.invoke(this.paintable, this.raise);
	}

	@Override
	public void setParent(final Paintable parent, final int x, final int y) {
		this.qFRenderEngine.invoke(this.paintable, this.paintInstructionFactory
				.createSetParentInstruction(parent, x, y));
	}

	@Override
	public void resize(final int width, final int height) {
		this.qFRenderEngine.invoke(this.paintable, this.paintInstructionFactory
				.createResizeInstruction(width, height));
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
	public void grabMouse() {
		this.qFRenderEngine.invoke(this.paintable, this.grabMouse);
	}

	@Override
	public void releaseKeyboard() {
		this.qFRenderEngine.invoke(this.paintable, this.releaseKeyboard);
	}

	@Override
	public void releaseMouse() {
		this.qFRenderEngine.invoke(this.paintable, this.releaseMouse);
	}

	@Override
	public Coordinates translateCoordinates(final Paintable source,
											final int sourceX,
											final int sourceY) {
		return this.qFRenderEngine
				.invoke(this.paintable, this.paintCalculationFactory
						.createTranslateCoordinatesCalculation(	source,
																sourceX,
																sourceY));
	}

	/*
	 * (non-Javadoc)
	 * @see org.hydrogen.paint.api.Painter#instruct(org.hydrogen.paint.api.
	 * PaintInstruction)
	 */
	@Override
	public void instruct(final PaintInstruction<? extends RenderEngine> paintInstruction) {
		this.qFRenderEngine.invoke(this.paintable, paintInstruction);
	}

	/*
	 * (non-Javadoc)
	 * @see org.hydrogen.paint.api.Painter#construct(org.hydrogen.paint.api.
	 * PaintConstruction)
	 */
	@Override
	public ResourceHandle construct(final PaintConstruction<? extends RenderEngine> paintConstruction) {
		return this.qFRenderEngine.invoke(this.paintable, paintConstruction);
	}

	/*
	 * (non-Javadoc)
	 * @see org.hydrogen.paint.api.Painter#calculate(org.hydrogen.paint.api.
	 * PaintCalculation)
	 */
	@Override
	public <R> R calculate(final PaintCalculation<R, ? extends RenderEngine> paintCalculation) {
		return this.qFRenderEngine.invoke(this.paintable, paintCalculation);
	}
}

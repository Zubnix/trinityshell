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
package org.fusion.paintengine.impl.painter;

import javax.inject.Named;

import org.fusion.paintengine.api.QFRenderEngine;
import org.fusion.paintengine.api.painter.QFMoveInstructionFactory;
import org.fusion.paintengine.api.painter.QFMoveResizeInstructionFactory;
import org.fusion.paintengine.api.painter.QFPaintConstruction;
import org.fusion.paintengine.api.painter.QFPaintInstruction;
import org.fusion.paintengine.api.painter.QFResizeInstructionFactory;
import org.fusion.paintengine.api.painter.QFSetParentInstructionFactory;
import org.fusion.paintengine.api.painter.QFTranslateCoordinatesCalculationFactory;
import org.hydrogen.display.api.ResourceHandle;
import org.hydrogen.geometry.api.Coordinates;
import org.hydrogen.paint.api.PaintCalculation;
import org.hydrogen.paint.api.PaintConstruction;
import org.hydrogen.paint.api.PaintInstruction;
import org.hydrogen.paint.api.Paintable;
import org.hydrogen.paint.api.Painter;
import org.hydrogen.paint.api.RenderEngine;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class QFusionPainter implements Painter {

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

	private final QFMoveInstructionFactory moveInstructionFactory;
	private final QFResizeInstructionFactory resizeInstructionFactory;
	private final QFMoveResizeInstructionFactory moveResizeInstructionFactory;
	private final QFSetParentInstructionFactory setParentInstructionFactory;

	private final QFTranslateCoordinatesCalculationFactory translateCoordinatesCalculationFactory;

	private final QFRenderEngine qFRenderEngine;
	private final Paintable paintable;

	@Inject
	protected QFusionPainter(	@Named("QFLower") final QFPaintInstruction lower,
								@Named("QFRaise") final QFPaintInstruction raise,
								@Named("QFHide") final QFPaintInstruction hide,
								@Named("QFShow") final QFPaintInstruction show,
								@Named("QFGrabKeyboard") final QFPaintInstruction grabKeyboard,
								@Named("QFReleaseKeyboard") final QFPaintInstruction releaseKeyboard,
								@Named("QFGrabMouse") final QFPaintConstruction grabMouse,
								@Named("QFReleaseMouse") final QFPaintConstruction releaseMouse,
								@Named("QFDestroy") final QFPaintConstruction destroy,
								@Named("QFGiveFocus") final QFPaintConstruction giveFocus,
								final QFMoveInstructionFactory moveInstructionFactory,
								final QFResizeInstructionFactory resizeInstructionFactory,
								final QFMoveResizeInstructionFactory moveResizeInstructionFactory,
								final QFSetParentInstructionFactory setParentInstructionFactory,
								final QFTranslateCoordinatesCalculationFactory translateCoordinatesCalculationFactory,
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

		this.moveInstructionFactory = moveInstructionFactory;
		this.resizeInstructionFactory = resizeInstructionFactory;
		this.moveResizeInstructionFactory = moveResizeInstructionFactory;
		this.setParentInstructionFactory = setParentInstructionFactory;

		this.translateCoordinatesCalculationFactory = translateCoordinatesCalculationFactory;

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
		this.qFRenderEngine.invoke(this.paintable, this.moveInstructionFactory
				.createMoveInstruction(x, y));
	}

	@Override
	public void moveResize(	final int x,
							final int y,
							final int width,
							final int height) {
		this.qFRenderEngine
				.invoke(this.paintable, this.moveResizeInstructionFactory
						.createMoveResizeInstruction(x, y, width, height));
	}

	@Override
	public void raise() {
		this.qFRenderEngine.invoke(this.paintable, this.raise);
	}

	@Override
	public void setParent(final Paintable parent, final int x, final int y) {
		this.qFRenderEngine.invoke(	this.paintable,
									this.setParentInstructionFactory
											.createSetParentInstruction(parent,
																		x,
																		y));
	}

	@Override
	public void resize(final int width, final int height) {
		this.qFRenderEngine
				.invoke(this.paintable, this.resizeInstructionFactory
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
				.invoke(this.paintable,
						this.translateCoordinatesCalculationFactory
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

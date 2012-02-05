/*
 * This file is part of HyperDrive.
 * 
 * HyperDrive is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * HyperDrive is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * HyperDrive. If not, see <http://www.gnu.org/licenses/>.
 */
package org.hyperdrive.widget;

import org.hydrogen.displayinterface.input.MouseInput;
import org.hyperdrive.geo.GeoTransformableRectangle;
import org.hyperdrive.input.ManagedMouse;

// TODO create abstract push button class
/**
 * A <code>DragButton</code> can move another
 * <code>GeoTransformableRectangle</cod>, defined by the target renderarea.
 * <p>
 * A <code>DragButton</code> monitors a <code>ManagedMouse</code> when the
 * <code>DragButton</code> is activated. Should the <code>ManagedMouse</code>
 * change it's position, the <code>DragButton</code> will move the target
 * <code>GeoTransformableRectangle</code> accordingly. A <code>DragButton</code>
 * will stop monitoring the <code>ManagedMouse</code> as soon as it is
 * deactivated.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class DragButton extends Button {

	private volatile boolean started;
	private GeoTransformableRectangle targetRenderArea;
	private int x0;
	private int y0;

	/**
	 * 
	 */
	public DragButton() {
		stopDrag();
	}

	/**
	 * Indicates of this <code>DragButton</code> is active or not.
	 * <p>
	 * An active </code>DragButton</code> mutates it's target
	 * <code>AbstractRenderArea</code>.
	 * 
	 * @return True if this <code>DragButton</code> is active. false if not.
	 */
	public boolean isStarted() {
		return this.started;
	}

	/**
	 * 
	 * @return
	 */
	protected int getX0() {
		return this.x0;
	}

	/**
	 * 
	 * @return
	 */
	protected int getY0() {
		return this.y0;
	}

	/**
	 * 
	 * @param x0
	 */
	protected void setX0(final int x0) {
		this.x0 = x0;
	}

	/**
	 * 
	 * @param y0
	 */
	protected void setY0(final int y0) {
		this.y0 = y0;
	}

	private void doDragCycles(final ManagedMouse mousePointer) {
		while (isStarted()) {
			mousePointer.refresh();

			final int x1 = mousePointer.getAbsoluteX();
			final int y1 = mousePointer.getAbsoluteY();

			final int deltaX = x1 - getX0();
			final int deltaY = y1 - getY0();

			if ((deltaX != 0) || (deltaY != 0)) {
				setX0(x1);
				setY0(y1);
				mutate(deltaX, deltaY);
			}

			// Process the next event without blocking, else we will never
			// receive the mouse button released event. This would result in
			// an endless loop where the target window would be "glued" to
			// the mouse cursor.
			getManagedDisplay().getEventDispatcher().dispatchNextEvent(false);
		}
	}

	/**
	 * The target <code>GeoTransformableRectangle</code> of this
	 * <code>DragButton</code>.
	 * <p>
	 * The target <code>GeoTransformableRectangle</code> will be manipulated if
	 * this <code>Widget</code> is activated</code>.
	 * 
	 * @return A {@link GeoTransformableRectangle}.
	 */
	public GeoTransformableRectangle getTargetRenderArea() {
		return this.targetRenderArea;
	}

	/**
	 * Mutate the target <code>GeoTransformableRectangle</code> with the given
	 * delta values.
	 * <p>
	 * The given delta values indicate the movement of the
	 * <code>ManagedMouse</code>.
	 * 
	 * @param deltaX
	 *            The horizontal movement of the <code>ManagedMouse</code> in
	 *            pixels.
	 * @param deltaY
	 *            The vertical movement of the <code>ManagedMouse</code> in
	 *            pixels. @ Thrown when the target
	 *            <code>AbstractRenderArea</code> has an illegal state.
	 */
	protected void mutate(final int deltaX, final int deltaY) {
		getTargetRenderArea().setRelativeX(
				getTargetRenderArea().getRelativeX() + deltaX);
		getTargetRenderArea().setRelativeY(
				getTargetRenderArea().getRelativeY() + deltaY);
		getTargetRenderArea().requestMove();
	}

	/**
	 * Set the target <code>GeoTransformableRectangle</code> that will be
	 * manipulated if this <code>DragButton</code> is activated</code>.
	 * 
	 * @param targetRenderArea
	 */
	public void setTargetRenderArea(
			final GeoTransformableRectangle targetRenderArea) {
		this.targetRenderArea = targetRenderArea;
	}

	/**
	 * Activate the <code>ManagedMouse</code> movement monitor for this
	 * <code>DragButton</code>.
	 * <p>
	 * Any movement will be propagated to the <code>mutate(int,int)</code>
	 * method of this <code>DragButton</code>.
	 * 
	 * @param input
	 */
	public void startDrag(final MouseInput input) {
		if (!isStarted()) {
			final ManagedMouse mousePointer = getManagedDisplay()
					.getManagedMouse();
			setX0(input.getRootX());
			setY0(input.getRootY());

			this.started = true;

			doDragCycles(mousePointer);
			stopDrag();
		}
	}

	/**
	 * Stop the <code>ManagedMouse</code> movement monitor for this
	 * <code>DragButton</code>.
	 */
	public void stopDrag() {
		this.started = false;
	}

	@Override
	public void onMouseButtonReleased(final MouseInput input) {
		stopDrag();
	}

	@Override
	public void onMouseButtonPressed(final MouseInput input) {
		startDrag(input);
	}

	@Override
	protected View initView(final ViewFactory<?> viewFactory) {
		return viewFactory.newDragButtonView();
	}
}

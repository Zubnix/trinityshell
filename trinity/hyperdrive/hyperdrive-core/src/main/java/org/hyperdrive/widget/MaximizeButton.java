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

// TODO create push button superclass
/**
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class MaximizeButton extends Button {

	@ViewDefinition
	public interface View extends Button.View {

	}

	// TODO define a generic rectangle interface
	public static interface MaximizeArea {
		int getAbsoluteX();

		int getAbsoluteY();

		int getWidth();

		int getHeight();
	}

	private GeoTransformableRectangle targetRenderArea;
	private MaximizeArea maximizeToArea;

	private int oldRelX;
	private int oldRelY;
	private int oldWidth;
	private int oldHeight;
	private boolean maximized;

	public MaximizeButton() {
		this.maximized = false;
	}

	/**
	 * The target <code>GeoTransformableRectangle</code> that will be maximized
	 * when this <code>MaximizeButton</code> is activated.
	 * 
	 * @return
	 */
	public GeoTransformableRectangle getTargetRenderArea() {
		return this.targetRenderArea;
	}

	/**
	 * Define the target <code>GeoTransformableRectangle</code> that will be
	 * maximized when this <code>MaximizeButton</code> is activated.
	 * 
	 * @param targetRenderArea
	 */
	public void setTargetRenderArea(
			final GeoTransformableRectangle targetRenderArea) {
		this.targetRenderArea = targetRenderArea;
	}

	/**
	 * 
	 * @param maximizeToArea
	 */
	public void setMaximizeToArea(final MaximizeArea maximizeToArea) {
		this.maximizeToArea = maximizeToArea;
	}

	/**
	 * 
	 * @return
	 */
	public MaximizeArea getMaximizeToArea() {
		return this.maximizeToArea;
	}

	/**
	 * 
	 */
	public void maximize() {
		if (this.maximized) {
			getTargetRenderArea().setRelativeX(this.oldRelX);
			getTargetRenderArea().setRelativeY(this.oldRelY);
			getTargetRenderArea().setWidth(this.oldWidth);
			getTargetRenderArea().setHeight(this.oldHeight);

			this.maximized = false;
		} else {
			final int newAbsX = getMaximizeToArea().getAbsoluteX();
			final int newAbsY = getMaximizeToArea().getAbsoluteY();
			final int newWidth = getMaximizeToArea().getWidth();
			final int newHeight = getMaximizeToArea().getHeight();

			final int currentAbsX = getTargetRenderArea().getAbsoluteX();
			final int currentabsY = getTargetRenderArea().getAbsoluteY();
			final int currentRelX = getTargetRenderArea().getRelativeX();
			final int currentRelY = getTargetRenderArea().getRelativeY();

			this.oldRelX = currentRelX;
			this.oldRelY = currentRelY;
			this.oldWidth = getTargetRenderArea().getWidth();
			this.oldHeight = getTargetRenderArea().getHeight();

			final int deltaX = newAbsX - currentAbsX;
			final int deltaY = newAbsY - currentabsY;
			final int newRelX = currentRelX + deltaX;
			final int newRelY = currentRelY + deltaY;

			getTargetRenderArea().setRelativeX(newRelX);
			getTargetRenderArea().setRelativeY(newRelY);
			getTargetRenderArea().setWidth(newWidth);
			getTargetRenderArea().setHeight(newHeight);

			this.maximized = true;
		}
		getTargetRenderArea().requestMoveResize();
	}

	@Override
	public void onMouseButtonPressed(final MouseInput input) {
		maximize();
	}
}

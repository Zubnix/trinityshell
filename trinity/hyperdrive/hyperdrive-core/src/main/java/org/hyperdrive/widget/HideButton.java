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

// TODO create abstract push button class
/**
 * 
 * A <code>HideButton</code> can hide (unmap) another target
 * <code>GeoTransformableRectangle</code>. The target is specified by a call to
 * {@link HideButton#setTargetRenderArea(GeoTransformableRectangle)}. A hide is
 * initiated when a (any) mouse button is pressed on the <code>HideButton</code>
 * . This behaviour can be changed by overriding the
 * {@link HideButton#onMouseInput(MouseInput)} method.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class HideButton extends Button {

	private GeoTransformableRectangle targetRenderArea;

	@Override
	public void onMouseButtonPressed(final MouseInput input) {
		hideWindow();
	}

	/**
	 * Requests a <code>setVisibility(false)</code> on the target renderarea.
	 * 
	 */
	public void hideWindow() {
		getTargetRenderArea().setVisibility(false);
		getTargetRenderArea().requestVisibilityChange();
	}

	/**
	 * The target <code>GeoTransformableRectangle</code> that will be hidden
	 * when this <code>HideButton</coden> is activated.
	 * 
	 * @return
	 */
	public GeoTransformableRectangle getTargetRenderArea() {
		return this.targetRenderArea;
	}

	@Override
	protected View initView(final ViewFactory<?> viewFactory) {
		return viewFactory.newHideButtonView();
	}

	/**
	 * Define the target <code>GeoTransformableRectangle</code> that will be
	 * hidden when this <code>HideButton</code> is activated.
	 * 
	 * @param targetRenderArea
	 */
	public void setTargetRenderArea(
			final GeoTransformableRectangle targetRenderArea) {
		this.targetRenderArea = targetRenderArea;
	}
}
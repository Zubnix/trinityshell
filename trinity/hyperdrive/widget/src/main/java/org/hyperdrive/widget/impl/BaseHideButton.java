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
package org.hyperdrive.widget.impl;

import org.hydrogen.display.api.input.MouseInput;
import org.hydrogen.display.api.input.base.BaseMouseInput;
import org.hyperdrive.geo.api.GeoTransformableRectangle;
import org.hyperdrive.widget.api.HideButton;
import org.hyperdrive.widget.api.ViewReference;

// TODO create abstract push button class
/**
 * 
 * A <code>HideButton</code> can hide (unmap) another target
 * <code>GeoTransformableRectangle</code>. The target is specified by a call to
 * {@link BaseHideButton#setTargetRenderArea(GeoTransformableRectangle)}. A hide
 * is initiated when a (any) mouse button is pressed on the
 * <code>HideButton</code> . This behaviour can be changed by overriding the
 * {@link BaseHideButton#onMouseInput(BaseMouseInput)} method.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 * 
 */
public class BaseHideButton extends BaseButton implements HideButton {

	private GeoTransformableRectangle boundRectangle;

	@ViewReference
	private HideButton.View view;

	@Override
	public HideButton.View getView() {
		return view;
	}

	@Override
	public void onMouseButtonPressed(final MouseInput input) {
		hideClientWindow();
	}

	@Override
	public GeoTransformableRectangle getBoundRectangle() {
		return this.boundRectangle;
	}

	public void setBoundRectangle(final GeoTransformableRectangle boundRectangle) {
		this.boundRectangle = boundRectangle;
	}

	@Override
	public void hideClientWindow() {
		getBoundRectangle().setVisibility(false);
		getBoundRectangle().requestVisibilityChange();
	}
}
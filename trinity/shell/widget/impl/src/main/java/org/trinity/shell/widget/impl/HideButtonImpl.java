/*
 * This file is part of HyperDrive. HyperDrive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. HyperDrive is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with HyperDrive. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.trinity.shell.widget.impl;

import org.trinity.foundation.input.api.PointerInput;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.widget.api.HideButton;

import com.google.inject.Inject;
import com.google.inject.name.Named;

// TODO create abstract push button class
/**
 * A <code>HideButton</code> can hide (unmap) another target
 * <code>GeoTransformableRectangle</code>. The target is specified by a call to
 * {@link HideButtonImpl#setTargetRenderArea(GeoTransformableRectangle)}. A hide
 * is initiated when a (any) mouse button is pressed on the
 * <code>HideButton</code> . This behaviour can be changed by overriding the
 * {@link HideButtonImpl#onMouseInput(BaseMouseInput)} method.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class HideButtonImpl extends ButtonImpl implements HideButton {

	@Inject
	private HideButton.View view;

	private GeoTransformableRectangle boundRectangle;

	/*****************************************
	 * @param painterFactory
	 * @param view
	 ****************************************/
	@Inject
	protected HideButtonImpl(	final PainterFactory painterFactory,
								@Named("Widget") final GeoExecutor geoExecutor) {
		super(painterFactory, geoExecutor);
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.shell.widget.impl.ButtonImpl#getView()
	 */
	@Override
	public HideButton.View getView() {
		return this.view;
	}

	@Override
	public void onMouseButtonPressed(final PointerInput input) {
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
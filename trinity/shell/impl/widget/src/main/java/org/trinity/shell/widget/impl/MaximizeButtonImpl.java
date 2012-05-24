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

import org.trinity.core.geometry.api.Rectangle;
import org.trinity.core.input.api.MouseInput;
import org.trinity.core.render.api.PainterFactory;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.widget.api.MaximizeButton;

import com.google.inject.Inject;
import com.google.inject.name.Named;

// TODO create push button superclass
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class MaximizeButtonImpl extends ButtonImpl implements MaximizeButton {

	@Inject
	private MaximizeButton.View view;

	private GeoTransformableRectangle targetRenderArea;
	private Rectangle maximizeToArea;

	private int oldRelX;
	private int oldRelY;
	private int oldWidth;
	private int oldHeight;
	private boolean maximized = false;

	@Inject
	protected MaximizeButtonImpl(	final PainterFactory painterFactory,
									@Named("Widget") final GeoExecutor geoExecutor) {
		super(painterFactory, geoExecutor);
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.shell.widget.impl.ButtonImpl#getView()
	 */
	@Override
	public MaximizeButton.View getView() {
		return this.view;
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
	public void setTargetRenderArea(final GeoTransformableRectangle targetRenderArea) {
		this.targetRenderArea = targetRenderArea;
	}

	/**
	 * @param maximizeToArea
	 */
	public void setMaximizeToArea(final Rectangle maximizeToArea) {
		this.maximizeToArea = maximizeToArea;
	}

	/**
	 * @return
	 */
	public Rectangle getMaximizeToArea() {
		return this.maximizeToArea;
	}

	@Override
	public void onMouseButtonPressed(final MouseInput input) {
		if (this.maximized) {
			restoreClientWindow();
		} else {
			maximizeClientWindow();
		}
	}

	@Override
	public GeoTransformableRectangle getBoundRectangle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void maximizeClientWindow() {
		final int newRelX = getMaximizeToArea().getX();
		final int newRelY = getMaximizeToArea().getY();
		final int newWidth = getMaximizeToArea().getWidth();
		final int newHeight = getMaximizeToArea().getHeight();

		final int currentRelX = getTargetRenderArea().getX();
		final int currentRelY = getTargetRenderArea().getY();

		this.oldRelX = currentRelX;
		this.oldRelY = currentRelY;
		this.oldWidth = getTargetRenderArea().getWidth();
		this.oldHeight = getTargetRenderArea().getHeight();

		getTargetRenderArea().setX(newRelX);
		getTargetRenderArea().setY(newRelY);
		getTargetRenderArea().setWidth(newWidth);
		getTargetRenderArea().setHeight(newHeight);

		this.maximized = true;
	}

	@Override
	public void restoreClientWindow() {
		getTargetRenderArea().setX(this.oldRelX);
		getTargetRenderArea().setY(this.oldRelY);
		getTargetRenderArea().setWidth(this.oldWidth);
		getTargetRenderArea().setHeight(this.oldHeight);

		this.maximized = false;
	}
}

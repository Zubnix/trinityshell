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

import org.trinity.core.input.api.MouseInput;
import org.trinity.core.render.api.PainterFactory;
import org.trinity.shell.foundation.api.RenderArea;
import org.trinity.shell.foundation.impl.AbstractRenderArea;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.protocol.api.DesktopProtocol;
import org.trinity.shell.widget.api.CloseButton;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * A <code>CloseButton</code> can terminate another
 * <code>AbstractRenderArea</code> that is defined by the target
 * <code>AbstractRenderArea</code>.
 * <p>
 * In case that the target <code>AbstractRenderArea</code> is a
 * <code>ClientWindow</code>, the <code>CloseButton</code> will ask the
 * <code>ClientWindow</code> to shut down itself. This does not guaranty that
 * the <code>ClientWindow</code> will actually shut down since most
 * <code>ClientWindow</code>s will present the user with a dialog to confirm if
 * the <code>ClientWindow</code> should shut down.
 * <p>
 * If the <code>AbstractRenderArea</code> is a <code>Widget</code>, the
 * <code>AbstractRenderArea</code> will simply be destroyed or other as defined
 * by the <code>GeoManager</code> of the <code>Widget</code>.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
public class CloseButtonImpl extends ButtonImpl implements CloseButton {

	@Inject
	private CloseButton.View view;

	private RenderArea boundRectangle;
	private final DesktopProtocol desktopProtocol;

	@Inject
	protected CloseButtonImpl(	final PainterFactory painterFactory,
								@Named("Widget") final GeoExecutor geoExecutor,
								final DesktopProtocol desktopProtocol) {
		super(painterFactory, geoExecutor);
		this.desktopProtocol = desktopProtocol;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.shell.widget.impl.ButtonImpl#getView()
	 */
	@Override
	public CloseButton.View getView() {
		return this.view;
	}

	/**
	 * Define the target <code>AbstractRenderArea</code>.
	 * <p>
	 * The target <code>AbstractRenderArea</code> is the
	 * <code>AbstractRenderArea</code> which will be asked to terminate should
	 * this <code>CloseButton</code> be triggered.
	 * 
	 * @param targetRenderArea
	 *            A {@link AbstractRenderArea}.
	 */
	public void setTargetRenderArea(final RenderArea targetRenderArea) {
		this.boundRectangle = targetRenderArea;
	}

	@Override
	public void onMouseButtonPressed(final MouseInput input) {
		closeClientWindow();
	}

	@Override
	public RenderArea getBoundRectangle() {
		return this.boundRectangle;
	}

	public void setBoundRectangle(final RenderArea rectangle) {
		this.boundRectangle = rectangle;
	}

	@Override
	public void closeClientWindow() {
		this.desktopProtocol.requestDelete(getBoundRectangle());
	}
}

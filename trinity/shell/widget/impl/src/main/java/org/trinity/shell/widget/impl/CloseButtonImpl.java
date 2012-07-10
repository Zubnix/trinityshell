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

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.input.api.Momentum;
import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.core.api.ManagedDisplay;
import org.trinity.shell.core.api.RenderArea;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.event.GeoEventFactory;
import org.trinity.shell.widget.api.CloseButton;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

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
@Bind
public class CloseButtonImpl extends ButtonImpl implements CloseButton {

	private RenderArea client;
	private final DesktopProtocol desktopProtocol;
	private final CloseButton.View view;

	@Inject
	protected CloseButtonImpl(	final EventBus eventBus,
								final GeoEventFactory geoEventFactory,
								final ManagedDisplay managedDisplay,
								final PainterFactory painterFactory,
								@Named("Widget") final GeoExecutor geoExecutor,
								final DesktopProtocol desktopProtocol,
								final CloseButton.View view) {
		super(	eventBus,
				geoEventFactory,
				managedDisplay,
				painterFactory,
				geoExecutor,
				view);
		this.desktopProtocol = desktopProtocol;
		this.view = view;
	}

	@Override
	public void setClient(final RenderArea client) {
		this.client = client;
	}

	@Override
	public RenderArea getClient() {
		return this.client;
	}

	@Subscribe
	public void onMouseButtonPressed(final ButtonNotifyEvent input) {
		if (input.getInput().getMomentum() == Momentum.STARTED) {
			closeClient();
		}
	}

	@Override
	public void closeClient() {
		this.desktopProtocol.requestDelete(getClient());
		this.view.closeClient(getClient());
	}
}

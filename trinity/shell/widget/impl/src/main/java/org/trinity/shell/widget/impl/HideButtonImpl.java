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
import org.trinity.shell.core.api.ManagedDisplayService;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.geo.api.GeoTransformableRectangle;
import org.trinity.shell.geo.api.event.GeoEventFactory;
import org.trinity.shell.widget.api.HideButton;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

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
@Bind
public class HideButtonImpl extends ButtonImpl implements HideButton {

	private GeoTransformableRectangle client;
	private final HideButton.View view;

	/*****************************************
	 * @param painterFactory
	 * @param view
	 ****************************************/
	@Inject
	protected HideButtonImpl(	final EventBus eventBus,
								final GeoEventFactory geoEventFactory,
								final ManagedDisplayService managedDisplay,
								final PainterFactory painterFactory,
								@Named("Widget") final GeoExecutor geoExecutor,
								final HideButton.View view) {
		super(	eventBus,
				geoEventFactory,
				managedDisplay,
				painterFactory,
				geoExecutor,
				view);
		this.view = view;
	}

	@Subscribe
	public void onMouseButtonPressed(final ButtonNotifyEvent input) {
		if (input.getInput().getMomentum() == Momentum.STARTED) {
			hideClient();
		}
	}

	@Override
	public GeoTransformableRectangle getClient() {
		return this.client;
	}

	@Override
	public void setClient(final GeoTransformableRectangle client) {
		this.client = client;
	}

	@Override
	public void hideClient() {
		getClient().requestHide();
		this.view.hideClient(getClient());
	}
}
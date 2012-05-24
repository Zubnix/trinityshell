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

import org.trinity.core.display.api.event.ButtonNotifyEvent;
import org.trinity.core.render.api.PainterFactory;
import org.trinity.shell.foundation.api.ManagedDisplay;
import org.trinity.shell.foundation.api.event.MouseButtonReleasedHandler;
import org.trinity.shell.geo.api.GeoEventHandler;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.widget.api.Button;

import com.google.inject.Inject;
import com.google.inject.name.Named;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class ButtonImpl extends WidgetImpl implements Button {

	@Inject
	private Button.View view;

	/*****************************************
	 * @param painterFactory
	 * @param view
	 ****************************************/
	@Inject
	protected ButtonImpl(	final PainterFactory painterFactory,
							@Named("Widget") final GeoExecutor geoExecutor) {
		super(painterFactory, geoExecutor);
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.shell.widget.impl.WidgetImpl#getView()
	 */
	@Override
	public Button.View getView() {
		return this.view;
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.shell.geo.impl.AbstractGeoTransformableRectangle#
	 * addGeoEventHandler(org.trinity.shell.geo.api.GeoEventHandler)
	 */
	@Override
	public void addGeoEventHandler(final GeoEventHandler geoEventHandler) {
		// TODO Auto-generated method stub
		super.addGeoEventHandler(geoEventHandler);
	}

	@Override
	protected void setManagedDisplay(final ManagedDisplay managedDisplay) {
		super.setManagedDisplay(managedDisplay);
		getManagedDisplay()
				.addDisplayEventHandler(new MouseButtonReleasedHandler() {
					@Override
					public void handleEvent(final ButtonNotifyEvent event) {
						draw(getView().mouseButtonReleased(event.getInput()));
						ButtonImpl.this.onMouseButtonReleased(event.getInput());

					}
				});
	}
}

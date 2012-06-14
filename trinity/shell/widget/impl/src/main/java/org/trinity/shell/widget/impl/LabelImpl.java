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

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.geo.api.GeoExecutor;
import org.trinity.shell.widget.api.Label;

import com.google.inject.Inject;
import com.google.inject.name.Named;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
public class LabelImpl extends WidgetImpl implements Label {

	@Inject
	private Label.View view;

	/*****************************************
	 * @param painterFactory
	 * @param view
	 ****************************************/
	@Inject
	protected LabelImpl(final PainterFactory painterFactory,
						@Named("Widget") final GeoExecutor geoExecutor) {
		super(painterFactory, geoExecutor);
	}

	/*
	 * (non-Javadoc)
	 * @see org.trinity.shell.widget.impl.WidgetImpl#getView()
	 */
	@Override
	public Label.View getView() {
		return this.view;
	}

	@Override
	public void updateLabel(final String name) {
		draw(getView().labelUpdated(name));
	}

}

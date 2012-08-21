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
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.geo.api.ShellNodeExecutor;
import org.trinity.shell.widget.api.ShellLabel;
import org.trinity.shell.widget.api.view.ShellLabelView;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO documentation
/**
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind
public class ShellLabelImpl extends ShellWidgetImpl implements ShellLabel {

	private final ShellLabelView view;
	private String text;

	@Inject
	protected ShellLabelImpl(	final EventBus eventBus,
								final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
								final PainterFactory painterFactory,
								@Named("shellWidgetGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
								final ShellLabelView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				shellNodeExecutor,
				view);
		this.view = view;
	}

	@Override
	public void setText(final String text) {
		this.text = text;
		this.view.update(text);
	}

	@Override
	public String getText() {
		return this.text;
	}
}
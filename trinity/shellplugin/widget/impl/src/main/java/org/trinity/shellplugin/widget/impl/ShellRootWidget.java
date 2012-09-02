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
package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.render.api.PainterFactory;
import org.trinity.shell.api.ShellDisplayEventDispatcher;
import org.trinity.shell.api.geo.ShellNodeExecutor;
import org.trinity.shell.api.geo.manager.ShellLayoutManager;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shell.api.widget.ShellWidgetView;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

// TODO override binding
// TODO implement resizing+moving? (with XRANDR extension in x11)
/**
 * A <code>RealRoot</code> represents a <code>BaseShellWidget</code> that is
 * backed by the native root window. It is the base of the
 * <code>ShellNode</code> tree hierarchy for a <code>ManagedDisplay</code>.
 * Multiple <code>RealRoot</code> widgets can be constructed from the same
 * <code>ManagedDisplay</code> but will represent the same on-screen drawable,
 * it is thus recommended to use the
 * {@link ShellDisplay#getRealRootRenderArea()} method to reference the real
 * root.
 * 
 * @author Erik De Rijcke
 * @since 1.0
 */
@Bind(value = @Named("ShellRootWidget"))
@Singleton
public class ShellRootWidget extends BaseShellWidget {

	@Inject
	ShellRootWidget(final EventBus eventBus,
					final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
					final PainterFactory painterFactory,
					@Named("shellWidgetGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
					@Named("shellRootWidgetView") final ShellWidgetView view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory,
				shellNodeExecutor,
				view);
	}

	@Override
	public ShellLayoutManager getParentLayoutManager() {
		return getLayoutManager();
	}
}

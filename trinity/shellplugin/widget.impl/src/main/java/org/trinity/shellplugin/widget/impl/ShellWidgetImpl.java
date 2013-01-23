/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.trinity.shellplugin.widget.impl;

import org.trinity.foundation.api.render.PainterFactory;
import org.trinity.foundation.api.render.binding.model.ViewReference;
import org.trinity.foundation.api.render.binding.view.View;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shell.api.widget.ShellWidget;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind
@To(value = Type.INTERFACES, customs = ShellWidget.class)
public class ShellWidgetImpl extends BaseShellWidget {

	// private final ShellSurfaceParent shellRootSurface;
	private final View view;

	@Inject
	ShellWidgetImpl(final EventBus eventBus,
					final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
					final PainterFactory painterFactory,
					final View view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory);
		this.view = view;
	}

	@ViewReference
	public View getView() {
		return this.view;
	}

	@Override
	protected ShellLayoutManager getParentLayoutManager(final ShellNodeParent parent) {

		return getLayoutManager();
	}
}

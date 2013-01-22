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
import org.trinity.shell.api.surface.ShellSurfaceParent;
import org.trinity.shell.api.widget.BaseShellWidget;
import org.trinity.shellplugin.widget.api.ShellWidgetRoot;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ShellWidgetRootImpl extends BaseShellWidget implements ShellWidgetRoot {

	private final ShellSurfaceParent shellRootSurface;
	private final View view;

	@Inject
	ShellWidgetRootImpl(final EventBus eventBus,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcher,
						final PainterFactory painterFactory,
						@Named("ShellRootSurface") final ShellSurfaceParent shellRootSurface,
						final View view) {
		super(	eventBus,
				shellDisplayEventDispatcher,
				painterFactory);
		this.shellRootSurface = shellRootSurface;
		this.view = view;
	}

	@ViewReference
	public View getView() {
		return view;
	}

	@Override
	protected ShellLayoutManager getParentLayoutManager(final ShellNodeParent parent) {

		return getLayoutManager();
	}

	@Override
	public void construct() {
		setParent(this.shellRootSurface);
		doReparent(false);
		init();

		setWidth(this.shellRootSurface.getWidth());
		setHeight(this.shellRootSurface.getHeight());
		doResize();
	}
}

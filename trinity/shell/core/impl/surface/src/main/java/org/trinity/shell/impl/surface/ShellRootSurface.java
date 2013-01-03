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
package org.trinity.shell.impl.surface;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeParent;
import org.trinity.shell.api.node.manager.ShellLayoutManager;
import org.trinity.shell.api.surface.AbstractShellSurfaceParent;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceExecutor;
import org.trinity.shell.api.surface.ShellSurfaceParent;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;

@Bind(value = @Named("ShellRootSurface"), to = @To(value = To.Type.CUSTOM, customs = { ShellSurfaceParent.class,
		ShellSurface.class, ShellNodeParent.class, ShellNode.class }))
@Singleton
public class ShellRootSurface extends AbstractShellSurfaceParent {

	private final ShellSurfaceExecutor shellNodeExecutor;
	private final DisplaySurface displaySurface;

	@Inject
	ShellRootSurface(	final EventBus eventBus,
						final DisplayServer displayServer,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcher) {
		super(eventBus);
		this.shellNodeExecutor = new ShellSurfaceExecutorImpl(this);
		this.displaySurface = displayServer.getRootDisplayArea();
		syncGeoToDisplaySurface();
		shellDisplayEventDispatcher.registerDisplayEventSourceListener(	eventBus,
																		this.displaySurface);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public int getAbsoluteX() {
		return getX();
	}

	@Override
	public int getAbsoluteY() {
		return getY();
	}

	@Override
	protected ShellLayoutManager getParentLayoutManager(final ShellNodeParent parent) {
		return getLayoutManager();
	}

	@Override
	public ShellRootSurface getParent() {
		return this;
	}

	@Override
	public ShellSurfaceExecutor getShellNodeExecutor() {
		return this.shellNodeExecutor;
	}

	@Override
	public DisplaySurface getDisplaySurface() {
		return this.displaySurface;
	}
}
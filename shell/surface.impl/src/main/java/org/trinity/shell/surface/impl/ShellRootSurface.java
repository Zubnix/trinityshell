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
package org.trinity.shell.surface.impl;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.server.DisplayServer;
import org.trinity.foundation.api.display.server.DisplaySurface;
import org.trinity.shell.api.scene.ShellNode;
import org.trinity.shell.api.scene.ShellNodeExecutor;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.surface.AbstractShellSurfaceParent;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceParent;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;

@Bind(value = @Named("ShellRootSurface"), to = @To(value = To.Type.CUSTOM, customs = { ShellSurfaceParent.class,
		ShellSurface.class, ShellNodeParent.class, ShellNode.class }))
@Singleton
// TODO make threadSafe
@NotThreadSafe
public class ShellRootSurface extends AbstractShellSurfaceParent {

	private final ShellNodeExecutor shellNodeExecutor;
	private final DisplayServer displayServer;

	@Inject
	ShellRootSurface(final DisplayServer displayServer) {
		this.displayServer = displayServer;
		this.shellNodeExecutor = new ShellSurfaceExecutorImpl(this);
		syncGeoToDisplaySurface();
		shellDisplayEventDispatcher.registerDisplayEventTarget(	getNodeEventBus(),
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
	public ShellRootSurface getParent() {
		return this;
	}

	@Override
	public ShellNodeExecutor getShellNodeExecutor() {
		return this.shellNodeExecutor;
	}

	@Override
	public ListenableFuture<DisplaySurface> getDisplaySurface() {
		return this.displayServer.getRootDisplayArea();
	}
}
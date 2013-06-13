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

import javax.annotation.concurrent.ThreadSafe;

import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.shared.OwnerThread;
import org.trinity.shell.api.scene.ShellScene;
import org.trinity.shell.api.surface.AbstractShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceParent;

import com.google.common.util.concurrent.ListeningExecutorService;

// TODO documentation
/**
 * A <code>ShellClientSurface</code> wraps a {@link DisplaySurface} that was
 * created by an independent program. As such the visual content can not be
 * directly manipulated. A <code>ShellClientSurface</code> is the owner of the
 * <code>PlatformRenderArea</code> it wraps. A <code>ShellClientSurface</code>
 * provides functionality to manage and manipulate the geometry and visibility
 * of the <code>PlatformRenderArea</code> it wraps.
 */
@ThreadSafe
@OwnerThread("Shell")
public final class ShellClientSurface extends AbstractShellSurface {

	private final ShellSurfaceGeometryDelegateImpl shellSurfaceGeometryDelegateImpl;
	private final DisplaySurface displaySurface;

	ShellClientSurface(	final ShellScene shellScene,final ListeningExecutorService shellExecutor,
						final ShellSurfaceParent rootShellSurface,
						final DisplaySurface clientDisplaySurface) {
		super(shellScene,shellExecutor);
		this.displaySurface = clientDisplaySurface;
		this.shellSurfaceGeometryDelegateImpl = new ShellSurfaceGeometryDelegateImpl(this);

		syncGeoToDisplaySurface();

		setParent(rootShellSurface);
		doReparent();

		subscribeToDisplaySurfaceEvents();
	}

	// never reparent from the underlying native parent.
	@Override
	protected void doReparent(final boolean execute) {
		super.doReparent(false);
	}

	@Override
	public void handleDestroyNotifyEvent(final DestroyNotify destroyNotify) {
		unsubscribeToDisplaySurfaceEvents();
		super.handleDestroyNotifyEvent(destroyNotify);
	}

	@Override
	public ShellSurfaceGeometryDelegateImpl getShellNodeGeometryDelegate() {
		return this.shellSurfaceGeometryDelegateImpl;
	}

	@Override
	public DisplaySurface getDisplaySurfaceImpl() {
		return this.displaySurface;
	}
}
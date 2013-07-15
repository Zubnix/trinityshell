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
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.surface.AbstractShellSurface;

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
@ExecutionContext(ShellExecutor.class)
public final class ShellClientSurface extends AbstractShellSurface {

	private final ShellSurfaceGeometryDelegateImpl shellSurfaceGeometryDelegateImpl;
	private final DisplaySurface displaySurface;

	// created by a custom factory so inject annotations are not needed.
	ShellClientSurface(	ShellNodeParent rootShellNodeParent,
						final AsyncListenable shellScene,
						final ListeningExecutorService shellExecutor,
						final DisplaySurface clientDisplaySurface) {
		super(	rootShellNodeParent,
				shellScene,
				shellExecutor);
		this.displaySurface = clientDisplaySurface;
		this.shellSurfaceGeometryDelegateImpl = new ShellSurfaceGeometryDelegateImpl(this);
	}

	@Override
	public ShellSurfaceGeometryDelegateImpl getShellNodeGeometryDelegate() {
		return this.shellSurfaceGeometryDelegateImpl;
	}

	@Override
	public DisplaySurface getDisplaySurfaceImpl() {
		return this.displaySurface;
	}

	// repeated for package level visibility
	@Override
	protected void doMoveResize(final boolean execute) {
		super.doMoveResize(execute);
	}
}

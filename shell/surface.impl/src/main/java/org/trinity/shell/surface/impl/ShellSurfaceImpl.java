/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/
package org.trinity.shell.surface.impl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.google.inject.assistedinject.Assisted;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.surface.AbstractShellSurface;

import com.google.common.util.concurrent.ListeningExecutorService;

// TODO documentation
/**
 * A <code>ShellSurfaceImpl</code> wraps a {@link DisplaySurface} that was
 * created by an independent program. As such the visual content can not be
 * directly manipulated. A <code>ShellSurfaceImpl</code> is the owner of the
 * <code>PlatformRenderArea</code> it wraps. A <code>ShellSurfaceImpl</code>
 * provides functionality to manage and manipulate the geometry and visibility
 * of the <code>PlatformRenderArea</code> it wraps.
 */
@NotThreadSafe
@ExecutionContext(ShellExecutor.class)
public final class ShellSurfaceImpl extends AbstractShellSurface {

	private final ShellSurfaceGeometryDelegateImpl shellSurfaceGeometryDelegateImpl;
	private final DisplaySurface displaySurface;

	// created by a custom factory so inject annotations are not needed.
	ShellSurfaceImpl(@Nonnull final ShellNodeParent shellRootNode,
	                 @Nonnull @ShellScene final AsyncListenable shellScene,
	                 @Nonnull @ShellExecutor final ListeningExecutorService shellExecutor,
	                 @Nonnull @Assisted final DisplaySurface clientDisplaySurface) {
		super(	shellRootNode,
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

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

import java.util.concurrent.Callable;

import javax.annotation.concurrent.NotThreadSafe;

import org.trinity.foundation.api.display.server.DisplaySurface;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.surface.AbstractShellSurface;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

// TODO documentation
/**
 * A <code>ShellClientSurface</code> wraps a {@link DisplaySurface} that was
 * created by an independent program. As such the visual content can not be
 * directly manipulated. A <code>ShellClientSurface</code> is the owner of the
 * <code>PlatformRenderArea</code> it wraps. A <code>ShellClientSurface</code>
 * provides functionality to manage and manipulate the geometry and visibility
 * of the <code>PlatformRenderArea</code> it wraps.
 */
@NotThreadSafe
public class ShellClientSurface extends AbstractShellSurface {

	private class DestroyCallback {
		private final ShellDisplayEventDispatcher shellDisplayEventDispatcher;

		public DestroyCallback(final ShellDisplayEventDispatcher shellDisplayEventDispatcher) {
			this.shellDisplayEventDispatcher = shellDisplayEventDispatcher;
		}

		// method is used by node eventbus.
		@SuppressWarnings("unused")
		@Subscribe
		public void handleDestroyNotify(final ShellNodeDestroyedEvent destroyEvent) {
			this.shellDisplayEventDispatcher.unregisterAllDisplayEventTarget(getDisplaySurface());
		}
	}

	private final ShellSurfaceExecutorImpl shellSurfaceExecutorImpl;
	private final DisplaySurface displaySurface;

	@Inject
	ShellClientSurface(	@Named("ShellRootSurface") final ShellNodeParent root,
						@Assisted final DisplaySurface displaySurface) {
		this.displaySurface = displaySurface;
		this.shellSurfaceExecutorImpl = new ShellSurfaceExecutorImpl(this);

		shellDisplayEventDispatcher.registerDisplayEventTarget(	getNodeEventBus(),
																displaySurface);
		addShellNodeEventHandler(new DestroyCallback(shellDisplayEventDispatcher));

		setParent(root);
		doReparent(false);
		syncGeoToDisplaySurface();
	}

	@Override
	public ShellSurfaceExecutorImpl getShellNodeExecutor() {
		return this.shellSurfaceExecutorImpl;
	}

	@Override
	public ListenableFuture<DisplaySurface> getDisplaySurface() {
		// same thread, should block(?)
		return MoreExecutors.sameThreadExecutor().submit(new Callable<DisplaySurface>() {
			@Override
			public DisplaySurface call() throws Exception {

				return ShellClientSurface.this.displaySurface;
			}
		});
	}
}
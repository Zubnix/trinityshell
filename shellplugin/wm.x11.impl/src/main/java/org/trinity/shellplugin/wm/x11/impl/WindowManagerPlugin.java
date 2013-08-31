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

package org.trinity.shellplugin.wm.x11.impl;

import static com.google.common.util.concurrent.Futures.addCallback;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;

@Bind(multiple = true)
@Singleton
@ExecutionContext(ShellExecutor.class)
public class WindowManagerPlugin extends AbstractIdleService implements ShellPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(WindowManagerPlugin.class);
	private final Display display;
	private final ShellSurfaceFactory shellSurfaceFactory;

	@Inject
	WindowManagerPlugin(final ShellSurfaceFactory shellSurfaceFactory,
						final Display display) {

		this.display = display;
		this.shellSurfaceFactory = shellSurfaceFactory;
	}

	// called by shell executor.
	@Override
	protected void startUp() {
		// We register without specifying an executor. This
		// means our listener (@Subscribe method) will be
		// called by the "Display" thread.
		this.display.register(this);
		// search & manage existing clients on the display server.
		find();
	}

	// called by display executor
	@Subscribe
	public void handleCreationNotify(final DisplaySurfaceCreationNotify displaySurfaceCreationNotify) {
		handleClientDisplaySurface(displaySurfaceCreationNotify.getDisplaySurface());
	}

	// called by shell executor.
	@Override
	protected void shutDown() throws Exception {
		this.display.unregister(this);
	}

	// called by shell executor
	private void find() {
		final ListenableFuture<List<DisplaySurface>> clientDisplaySurfaces = this.display.getClientDisplaySurfaces();
		// called by display thread
		addCallback(clientDisplaySurfaces,
					new FutureCallback<List<DisplaySurface>>() {
						@Override
						public void onSuccess(final List<DisplaySurface> displaySurfaces) {
							for (final DisplaySurface displaySurface : displaySurfaces) {
								// FIXME filter out non client display surfaces
								// in the display impl.
								// handleClientDisplaySurface(displaySurface);
							}
						}

						@Override
						public void onFailure(final Throwable t) {
							LOG.error(	"Unable to query for existing display surfaces.",
										t);

						}
					});
	}

	// Called by display executor for new display surfaces.
	private void handleClientDisplaySurface(final DisplaySurface displaySurface) {
		final ShellSurface clientShellSurface = this.shellSurfaceFactory.createShellSurface(displaySurface);

	}

	@Override
	public int runlevel() {
		return 5;
	}
}

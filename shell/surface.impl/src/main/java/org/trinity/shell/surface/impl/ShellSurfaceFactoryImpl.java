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

import com.google.common.util.concurrent.ListeningExecutorService;
import org.apache.onami.autobind.annotations.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellScene;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

@Bind
@Singleton
@ThreadSafe
public class ShellSurfaceFactoryImpl implements ShellSurfaceFactory {

	private static final Logger LOG = LoggerFactory.getLogger(ShellSurfaceFactory.class);
	private final AsyncListenable shellScene;
	private final ListeningExecutorService shellExecutor;

	@Inject
	ShellSurfaceFactoryImpl(@ShellScene final AsyncListenable shellScene,
							@ShellExecutor final ListeningExecutorService shellExecutor) {
		this.shellScene = shellScene;
		this.shellExecutor = shellExecutor;
	}

	@Override
	public ShellSurface createShellSurface(@Nonnull final DisplaySurface displaySurface) {
        final ShellSurfaceImpl shellSurfaceImpl = new ShellSurfaceImpl(displaySurface,
                                                                       this.shellScene,
                                                                       this.shellExecutor);
        displaySurface.register(shellSurfaceImpl,
                                this.shellExecutor);

        return shellSurfaceImpl;

    }
}

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

import static org.apache.onami.autobind.annotations.To.Type.CUSTOM;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.display.Display;
import org.trinity.shell.api.plugin.ShellPlugin;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ListeningExecutorService;

@Bind(to = @To(value = CUSTOM, customs = ShellPlugin.class), multiple = true)
@Singleton
@NotThreadSafe
public class ShellSurfacePlugin extends AbstractIdleService implements ShellPlugin {

	private final Display display;

	@Inject
	ShellSurfacePlugin(
						@Nonnull final Display display) {
		this.display = display;
	}

	@Override
	protected void startUp() throws Exception {
	}

	@Override
	protected void shutDown() throws Exception {
		this.display.quit();
	}
}

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

import static org.apache.onami.autobind.annotations.To.Type.CUSTOM;

import java.util.concurrent.TimeUnit;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.display.Display;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.plugin.ShellPlugin;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Bind(multiple = true)
@To(value = CUSTOM, customs = ShellPlugin.class)
@Singleton
public class ShellSurfacePlugin extends AbstractIdleService implements ShellPlugin {

	private final Display display;
	private final ListeningExecutorService shellExecutor;

	@Inject
	ShellSurfacePlugin(	@ShellExecutor final ListeningExecutorService shellExecutor,
						final Display display) {
		this.display = display;
		this.shellExecutor = shellExecutor;
	}

	@Override
	protected void startUp() throws Exception {
	}

	@Override
	protected void shutDown() throws Exception {
		this.display.quit().get(2,
								TimeUnit.SECONDS);
		this.shellExecutor.shutdown();
		this.shellExecutor.awaitTermination(2,
											TimeUnit.SECONDS);
	}
}

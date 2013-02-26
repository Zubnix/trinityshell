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
package org.trinity.shell.plugin.impl;

import java.util.Set;

import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.plugin.ShellPluginsRunner;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ShellPluginsRunnerImpl implements ShellPluginsRunner {

	private final Set<ShellPlugin> shellPlugins;

	@Inject
	ShellPluginsRunnerImpl(final Set<ShellPlugin> shellPlugins) {
		this.shellPlugins = shellPlugins;
	}

	@Override
	public void startAll() {
		for (final ShellPlugin shellPlugin : this.shellPlugins) {
			shellPlugin.start();
		}
	}

	@Override
	public void stopAll() {
		for (final ShellPlugin shellPlugin : this.shellPlugins) {
			shellPlugin.stop();
		}
	}
}
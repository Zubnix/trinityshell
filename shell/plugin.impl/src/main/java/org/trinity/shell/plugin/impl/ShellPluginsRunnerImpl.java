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

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.plugin.ShellPluginsRunner;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import com.google.inject.Inject;
import com.google.inject.Singleton;

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
			shellPlugin.addListener(new Listener() {

										@Override
										public void terminated(final State from) {
											System.err.println(String.format(	"%s TERMINATED (was %s)",
																				shellPlugin,
																				from));
										}

										@Override
										public void stopping(final State from) {
											System.err.println(String.format(	"%s STOPPING (was %s)",
																				shellPlugin,
																				from));
										}

										@Override
										public void starting() {
											System.err.println(String.format(	"%s STARTING",
																				shellPlugin));
										}

										@Override
										public void running() {
											System.err.println(String.format(	"%s RUNNING",
																				shellPlugin));
										}

										@Override
										public void failed(	final State from,
															final Throwable failure) {
											System.err.println(String.format(	"%s FAILED (was %s)",
																				shellPlugin,
																				from));
											failure.printStackTrace();
										}
									},
									MoreExecutors.sameThreadExecutor());
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

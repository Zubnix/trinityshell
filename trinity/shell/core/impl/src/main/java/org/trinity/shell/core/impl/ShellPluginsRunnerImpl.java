package org.trinity.shell.core.impl;

import java.util.Set;

import org.trinity.shell.core.api.ShellPlugin;
import org.trinity.shell.core.api.ShellPluginsRunner;

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
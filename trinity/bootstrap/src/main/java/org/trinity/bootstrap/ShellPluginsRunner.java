package org.trinity.bootstrap;

import java.util.Set;

import org.trinity.shell.core.api.ShellPlugin;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(Type.IMPLEMENTATION))
@Singleton
public class ShellPluginsRunner {

	private final Set<ShellPlugin> shellPlugins;

	@Inject
	ShellPluginsRunner(final Set<ShellPlugin> shellPlugins) {
		this.shellPlugins = shellPlugins;
	}

	public void startAllShellPlugins() {
		for (final ShellPlugin shellPlugin : this.shellPlugins) {
			shellPlugin.start();
		}
	}

	public void stopAllShellPlugins() {
		for (final ShellPlugin shellPlugin : this.shellPlugins) {
			shellPlugin.stop();
		}
	}
}

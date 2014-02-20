package org.trinity.shellplugin.wm.x11.impl.scene;

import com.google.common.util.concurrent.AbstractIdleService;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shellplugin.wm.api.Shell;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class WmPlugin extends AbstractIdleService implements ShellPlugin {

	private final Shell shell;

	@Inject
	WmPlugin(final Shell shell) {
		this.shell = shell;
	}

	@Override
	protected void startUp() {
		this.shell.start();
	}

	@Override
	protected void shutDown() {
		this.shell.stop();
	}
}

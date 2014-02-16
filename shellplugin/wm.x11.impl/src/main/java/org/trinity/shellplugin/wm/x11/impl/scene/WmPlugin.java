package org.trinity.shellplugin.wm.x11.impl.scene;

import javax.inject.Inject;

import org.apache.onami.autobind.annotations.Bind;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shellplugin.wm.api.Shell;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Singleton;

/**
 *
 */
@Bind(multiple = true)
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

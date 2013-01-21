package org.trinity.shellplugin.wm.impl;

import org.trinity.shell.api.node.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.event.ShellSurfaceCreatedEvent;
import org.trinity.shellplugin.widget.api.ShellWidgetBar;
import org.trinity.shellplugin.widget.api.ShellWidgetRoot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class WindowManagerPlugin implements ShellPlugin {

	private final EventBus shellEventBus;
	private final ShellWidgetRoot shellWidgetRoot;
	private final ShellLayoutManagerLine shellLayoutManagerLine;
	private final Provider<ShellWidgetBar> shellWidgetBarProvider;

	@Inject
	WindowManagerPlugin(@Named("ShellEventBus") EventBus shellEventBus,
						ShellWidgetRoot shellWidgetRoot,
						ShellLayoutManagerLine shellLayoutManagerLine,
						Provider<ShellWidgetBar> shellWidgetBarProvider) {
		this.shellEventBus = shellEventBus;
		this.shellWidgetRoot = shellWidgetRoot;
		this.shellLayoutManagerLine = shellLayoutManagerLine;
		this.shellWidgetBarProvider = shellWidgetBarProvider;
	}

	@Override
	public void start() {
		shellWidgetRoot.construct();
		shellLayoutManagerLine.setHorizontalDirection(false);
		shellLayoutManagerLine.setInverseDirection(false);
		shellWidgetRoot.setLayoutManager(shellLayoutManagerLine);

		// TODO Setup topbar & bottombar widgets
		ShellWidgetBar topBar = shellWidgetBarProvider.get();

		this.shellEventBus.register(this);

	}

	@Override
	public void stop() {
		this.shellEventBus.unregister(this);
		shellWidgetRoot.doDestroy();
	}

	@Subscribe
	public void handleShellSurfaceCreated(ShellSurfaceCreatedEvent shellSurfaceCreatedEvent) {
		ShellSurface shellSurface = shellSurfaceCreatedEvent.getClient();
		// TODO manage client
	}
}

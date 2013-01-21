package org.trinity.shellplugin.wm.impl;

import org.trinity.foundation.api.shared.Margins;
import org.trinity.shell.api.node.ShellNodeParent;
import org.trinity.shell.api.node.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.node.manager.ShellLayoutPropertyLine;
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
	private final Provider<ShellNodeParent> shellVirtualNodeProvider;

	private ShellWidgetBar topBar;
	private ShellNodeParent clientDisplayArea;
	private ShellWidgetBar bottomBar;

	@Inject
	WindowManagerPlugin(@Named("ShellEventBus") EventBus shellEventBus,
						ShellWidgetRoot shellWidgetRoot,
						ShellLayoutManagerLine shellLayoutManagerLine,
						Provider<ShellWidgetBar> shellWidgetBarProvider,
						@Named("ShellVirtualNode") Provider<ShellNodeParent> shellVirtualNodeProvider) {
		this.shellEventBus = shellEventBus;
		this.shellWidgetRoot = shellWidgetRoot;
		this.shellLayoutManagerLine = shellLayoutManagerLine;
		this.shellWidgetBarProvider = shellWidgetBarProvider;
		this.shellVirtualNodeProvider = shellVirtualNodeProvider;
	}

	@Override
	public void start() {
		shellWidgetRoot.construct();
		shellLayoutManagerLine.setHorizontalDirection(false);
		shellLayoutManagerLine.setInverseDirection(false);
		shellWidgetRoot.setLayoutManager(shellLayoutManagerLine);

		// setup topbar, client display area and bottombar.
		// topbar
		topBar = shellWidgetBarProvider.get();
		ShellLayoutPropertyLine layoutPropertyTopBar = new ShellLayoutPropertyLine(	0,
																					Margins.NO_MARGINS);
		shellLayoutManagerLine.addChildNode(topBar,
											layoutPropertyTopBar);
		topBar.setParent(shellWidgetRoot);

		// client display area
		clientDisplayArea = shellVirtualNodeProvider.get();
		ShellLayoutPropertyLine layoutPropertyClientDisplayArea = new ShellLayoutPropertyLine(	1,
																								new Margins(5));
		shellLayoutManagerLine.addChildNode(clientDisplayArea,
											layoutPropertyClientDisplayArea);
		clientDisplayArea.setParent(shellWidgetRoot);

		// bottombar
		bottomBar = shellWidgetBarProvider.get();
		ShellLayoutPropertyLine layoutPropertyBottomBar = new ShellLayoutPropertyLine(	0,
																						Margins.NO_MARGINS);
		shellLayoutManagerLine.addChildNode(bottomBar,
											layoutPropertyBottomBar);
		topBar.setParent(shellWidgetRoot);

		shellWidgetRoot.doShow();

		this.shellEventBus.register(this);
	}

	@Override
	public void stop() {
		this.shellEventBus.unregister(this);
		shellWidgetRoot.doHide();
		shellWidgetRoot.doDestroy();
	}

	@Subscribe
	public void handleShellSurfaceCreated(ShellSurfaceCreatedEvent shellSurfaceCreatedEvent) {
		ShellSurface shellSurface = shellSurfaceCreatedEvent.getClient();
		// TODO manage client
	}
}

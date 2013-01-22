package org.trinity.shellplugin.wm.impl;

import org.trinity.foundation.api.shared.Margins;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;
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

// TODO split this class up
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
	WindowManagerPlugin(@Named("ShellEventBus") final EventBus shellEventBus,
						final ShellWidgetRoot shellWidgetRoot,
						final ShellLayoutManagerLine shellLayoutManagerLine,
						final Provider<ShellWidgetBar> shellWidgetBarProvider,
						@Named("ShellVirtualNode") final Provider<ShellNodeParent> shellVirtualNodeProvider) {
		this.shellEventBus = shellEventBus;
		this.shellWidgetRoot = shellWidgetRoot;
		this.shellLayoutManagerLine = shellLayoutManagerLine;
		this.shellWidgetBarProvider = shellWidgetBarProvider;
		this.shellVirtualNodeProvider = shellVirtualNodeProvider;
	}

	@Override
	public void start() {
		this.shellWidgetRoot.construct();
		this.shellLayoutManagerLine.setHorizontalDirection(false);
		this.shellLayoutManagerLine.setInverseDirection(false);
		this.shellWidgetRoot.setLayoutManager(this.shellLayoutManagerLine);

		// setup topbar, client display area and bottombar.
		// topbar
		this.topBar = this.shellWidgetBarProvider.get();
		this.topBar.setHeight(25);
		this.topBar.doResize();
		final ShellLayoutPropertyLine layoutPropertyTopBar = new ShellLayoutPropertyLine(	0,
																							Margins.NO_MARGINS);
		this.shellLayoutManagerLine.addChildNode(	this.topBar,
													layoutPropertyTopBar);
		this.topBar.setParent(this.shellWidgetRoot);

		// client display area
		this.clientDisplayArea = this.shellVirtualNodeProvider.get();
		final ShellLayoutPropertyLine layoutPropertyClientDisplayArea = new ShellLayoutPropertyLine(1,
																									new Margins(5));
		this.shellLayoutManagerLine.addChildNode(	this.clientDisplayArea,
													layoutPropertyClientDisplayArea);
		this.clientDisplayArea.setParent(this.shellWidgetRoot);

		// bottombar
		this.bottomBar = this.shellWidgetBarProvider.get();
		this.bottomBar.setHeight(25);
		this.bottomBar.doResize();
		final ShellLayoutPropertyLine layoutPropertyBottomBar = new ShellLayoutPropertyLine(0,
																							Margins.NO_MARGINS);
		this.shellLayoutManagerLine.addChildNode(	this.bottomBar,
													layoutPropertyBottomBar);
		this.topBar.setParent(this.shellWidgetRoot);

		this.shellWidgetRoot.doShow();

		this.shellEventBus.register(this);
	}

	@Override
	public void stop() {
		this.shellEventBus.unregister(this);
		this.shellWidgetRoot.doHide();
		this.shellWidgetRoot.doDestroy();
	}

	@Subscribe
	public void handleShellSurfaceCreated(final ShellSurfaceCreatedEvent shellSurfaceCreatedEvent) {
		final ShellSurface shellSurface = shellSurfaceCreatedEvent.getClient();
		// TODO manage client
	}
}

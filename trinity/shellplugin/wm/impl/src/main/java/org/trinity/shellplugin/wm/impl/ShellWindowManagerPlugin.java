package org.trinity.shellplugin.wm.impl;

import org.trinity.foundation.shared.geometry.api.Margins;
import org.trinity.shell.api.ShellPlugin;
import org.trinity.shell.api.ShellSurface;
import org.trinity.shell.api.event.ShellSurfaceCreatedEvent;
import org.trinity.shell.api.geo.manager.ShellLayoutManager;
import org.trinity.shell.api.geo.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.widget.ShellWidget;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class ShellWindowManagerPlugin implements ShellPlugin {

	private final ShellSurface shellRootSurface;
	private final ShellWidget shellRootWidget;
	private final EventBus shellEventBus;

	private final ShellLayoutManager shellLayoutManager;

	@Inject
	ShellWindowManagerPlugin(	@Named("ShellRootSurface") final ShellSurface shellRootSurface,
								@Named("ShellRootWidget") final ShellWidget shellRootWidget,
								@Named("shellEventBus") final EventBus shellEventBus,
								@Named("ShellLayoutManagerLine") final ShellLayoutManager shellLayoutManager) {
		this.shellRootSurface = shellRootSurface;
		this.shellRootWidget = shellRootWidget;
		this.shellEventBus = shellEventBus;

		this.shellLayoutManager = shellLayoutManager;
	}

	@Subscribe
	public void handleShellClientCreated(final ShellSurfaceCreatedEvent shellSurfaceCreatedEvent) {
		// TODO create widget to move/resize/close client
		final ShellSurface client = shellSurfaceCreatedEvent.getClient();
		client.setParent(this.shellRootWidget);
		client.doReparent();

		this.shellLayoutManager.addChildNode(	client,
												new ShellLayoutPropertyLine(1,
																			new Margins(5)));
		this.shellLayoutManager.layout(this.shellRootSurface);
	}

	public void setupRootWidget() {
		this.shellRootWidget.init(null);
		this.shellRootWidget.setWidth(this.shellRootSurface.getWidth());
		this.shellRootWidget.setHeight(this.shellRootSurface.getHeight());
		this.shellRootWidget.doResize();
		this.shellRootWidget.doShow();
		this.shellRootWidget.setParent(this.shellRootSurface);
		this.shellRootWidget.doReparent();
		this.shellRootWidget.setLayoutManager(this.shellLayoutManager);
	}

	@Override
	public void start() {
		setupRootWidget();
		// TODO ask for all known client shell surfaces & manage them
		this.shellEventBus.register(this);
	}

	@Override
	public void stop() {
		// TODO delete all created shell widgets
		this.shellEventBus.unregister(this);
	}
}

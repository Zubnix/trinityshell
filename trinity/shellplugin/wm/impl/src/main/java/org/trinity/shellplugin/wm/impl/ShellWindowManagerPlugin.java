package org.trinity.shellplugin.wm.impl;

import org.trinity.foundation.shared.geometry.api.Margins;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.manager.ShellLayoutManager;
import org.trinity.shell.api.node.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.event.ShellSurfaceCreatedEvent;
import org.trinity.shell.api.widget.ShellWidget;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class ShellWindowManagerPlugin implements ShellPlugin {

	private final ShellSurface shellRootSurface;
	private final ShellWidget shellRootWidget;
	private final EventBus shellEventBus;

	private final Provider<ShellLayoutManager> shellLayoutManagerProvider;
	private final Provider<ShellNode> shellNodeProvider;
	private final Provider<ShellWidget> shellWidgetContainerProvider;

	private final ShellLayoutManager shellLayoutManager;

	private ShellNode virtualNode;

	@Inject
	ShellWindowManagerPlugin(	@Named("ShellRootSurface") final ShellSurface shellRootSurface,
								@Named("ShellRootWidget") final ShellWidget shellRootWidget,
								@Named("shellEventBus") final EventBus shellEventBus,
								@Named("ShellLayoutManagerLine") final Provider<ShellLayoutManager> shellLayoutManagerProvider,
								@Named("ShellWidgetContainer") final Provider<ShellWidget> shellWidgetContainerProvider,
								@Named("ShellVirtualNode") final Provider<ShellNode> shellNodeProvider) {
		this.shellRootSurface = shellRootSurface;
		this.shellRootWidget = shellRootWidget;
		this.shellEventBus = shellEventBus;

		this.shellLayoutManagerProvider = shellLayoutManagerProvider;
		this.shellNodeProvider = shellNodeProvider;
		this.shellWidgetContainerProvider = shellWidgetContainerProvider;

		this.shellLayoutManager = shellLayoutManagerProvider.get();
	}

	@Subscribe
	public void handleShellClientCreated(final ShellSurfaceCreatedEvent shellSurfaceCreatedEvent) {
		// TODO create widget to move/resize/close client?
		final ShellSurface client = shellSurfaceCreatedEvent.getClient();
		client.setParent(this.virtualNode);
		client.doReparent();

		this.shellLayoutManager.addChildNode(	client,
												new ShellLayoutPropertyLine(1,
																			new Margins(5)));
		this.virtualNode.layout();
	}

	public void setupRootWidget() {
		this.shellRootWidget.init(null);

		this.shellRootWidget.setWidth(this.shellRootSurface.getWidth());
		this.shellRootWidget.setHeight(this.shellRootSurface.getHeight());
		this.shellRootWidget.doResize();

		final ShellLayoutManager rootLayoutManager = this.shellLayoutManagerProvider.get();
		this.shellRootWidget.setLayoutManager(rootLayoutManager);

		final ShellWidget taskbar = this.shellWidgetContainerProvider.get();
		rootLayoutManager.addChildNode(	taskbar,
										new ShellLayoutPropertyLine(0,
																	new Margins(2)));
		taskbar.setParent(this.shellRootWidget);
		taskbar.doReparent();
		taskbar.setWidth(100);
		taskbar.doResize();
		taskbar.doShow();

		this.virtualNode = this.shellNodeProvider.get();
		this.virtualNode.setLayoutManager(this.shellLayoutManager);
		rootLayoutManager.addChildNode(	this.virtualNode,
										new ShellLayoutPropertyLine(1,
																	new Margins(0)));
		this.virtualNode.setParent(this.shellRootWidget);
		this.virtualNode.doReparent();
		this.virtualNode.doShow();

		this.shellRootWidget.layout();

		this.shellRootWidget.doShow();
	}

	@Override
	public void start() {
		setupRootWidget();
		// TODO ask for all existing client shell surfaces & manage them

		this.shellEventBus.register(this);
	}

	@Override
	public void stop() {
		// TODO delete all created shell widgets (if any)

		this.shellEventBus.unregister(this);
	}
}

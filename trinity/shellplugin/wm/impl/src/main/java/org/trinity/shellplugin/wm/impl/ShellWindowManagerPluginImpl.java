package org.trinity.shellplugin.wm.impl;

import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeParent;
import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.event.ShellSurfaceCreatedEvent;
import org.trinity.shell.api.widget.ShellRootWidget;
import org.trinity.shellplugin.wm.api.ShellDecorator;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class ShellWindowManagerPluginImpl implements ShellPlugin {

	private final ShellRootWidget shellRootWidget;
	private final EventBus shellEventBus;

	private final ShellDecorator shellDecorator;
	private ShellNodeParent rootFrame;

	@Inject
	ShellWindowManagerPluginImpl(	final ShellRootWidget shellRootWidget,
									@Named("shellEventBus") final EventBus shellEventBus,
									final ShellDecorator shellDecorator) {

		this.shellRootWidget = shellRootWidget;
		this.shellEventBus = shellEventBus;
		this.shellDecorator = shellDecorator;
	}

	@Subscribe
	public void handleShellClientCreated(final ShellSurfaceCreatedEvent shellSurfaceCreatedEvent) {

		final ShellSurface client = shellSurfaceCreatedEvent.getClient();
		final ShellNode clientFrame = this.shellDecorator.decorateClientSurface(client);
		clientFrame.setParent(this.rootFrame);
		clientFrame.doReparent();
		this.rootFrame.getLayoutManager().addChildNode(clientFrame);
		this.rootFrame.layout();
	}

	public void setupRootWidget() {
		this.shellRootWidget.construct();
		this.rootFrame = this.shellDecorator.decorateRootShellWidget(this.shellRootWidget);
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

/*
 * Trinity Window Manager and Desktop Shell Copyright (C) 2012 Erik De Rijcke
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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

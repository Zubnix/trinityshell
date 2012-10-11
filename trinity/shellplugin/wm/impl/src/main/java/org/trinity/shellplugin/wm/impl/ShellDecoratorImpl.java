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

import org.trinity.foundation.shared.geometry.api.Margins;
import org.trinity.shell.api.node.ShellNode;
import org.trinity.shell.api.node.ShellNodeParent;
import org.trinity.shell.api.node.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.node.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.widget.ShellWidget;
import org.trinity.shellplugin.widget.api.ShellWidgetStyled;
import org.trinity.shellplugin.wm.api.ShellDecorator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@Singleton
public class ShellDecoratorImpl implements ShellDecorator {

	private final Provider<ShellNodeParent> shellVirtualNodeProvider;
	private final Provider<ShellWidgetStyled> shellWidgetContainerProvider;
	private final Provider<ShellLayoutManagerLine> shellLayoutManagerLineProvider;

	@Inject
	public ShellDecoratorImpl(	@Named("ShellVirtualNode") final Provider<ShellNodeParent> shellNodeParentProvider,
								@Named("ShellWidgetContainer") final Provider<ShellWidgetStyled> shellWidgetContainerProvider,
								final Provider<ShellLayoutManagerLine> shellLayoutManagerLineProvider) {
		this.shellVirtualNodeProvider = shellNodeParentProvider;
		this.shellWidgetContainerProvider = shellWidgetContainerProvider;
		this.shellLayoutManagerLineProvider = shellLayoutManagerLineProvider;
	}

	@Override
	public ShellNode decorateClientSurface(final ShellSurface client) {

		final int clientX = client.getX();
		final int clientY = client.getY();

		final ShellNodeParent clientFrame = this.shellVirtualNodeProvider.get();
		clientFrame.setX(clientX);
		clientFrame.setY(clientY);
		clientFrame.doMove();

		final ShellLayoutManagerLine shellLayoutManagerLine = this.shellLayoutManagerLineProvider.get();
		shellLayoutManagerLine.setHorizontalDirection(false);
		shellLayoutManagerLine.setInverseDirection(false);
		clientFrame.setLayoutManager(shellLayoutManagerLine);

		final ShellWidgetStyled shellWidgetContainer = this.shellWidgetContainerProvider.get();
		shellWidgetContainer.setName("clientBar");
		shellWidgetContainer.setHeight(25);
		shellWidgetContainer.doResize();
		shellWidgetContainer.setParent(clientFrame);
		shellWidgetContainer.doReparent();
		shellWidgetContainer.doShow();

		client.setParent(clientFrame);
		client.doReparent();

		shellLayoutManagerLine.addChildNode(client,
											new ShellLayoutPropertyLine(1,
																		new Margins(0)));
		shellLayoutManagerLine.addChildNode(shellWidgetContainer,
											new ShellLayoutPropertyLine(0,
																		new Margins(0)));
		clientFrame.layout();

		new ShellNodeVisibilityMirror(	client,
										clientFrame);
		new ShellNodeDestroyMirror(	client,
									clientFrame);

		if (client.isVisible()) {
			clientFrame.doShow();
		}

		return clientFrame;
	}

	@Override
	public ShellNodeParent decorateRootShellWidget(final ShellWidget root) {
		final ShellLayoutManagerLine shellLayoutManagerLine = this.shellLayoutManagerLineProvider.get();
		shellLayoutManagerLine.setHorizontalDirection(false);
		shellLayoutManagerLine.setInverseDirection(false);
		root.setLayoutManager(shellLayoutManagerLine);

		final ShellWidgetStyled shellWidgetContainer = this.shellWidgetContainerProvider.get();
		shellWidgetContainer.setName("mainBar");

		shellWidgetContainer.setHeight(25);
		shellWidgetContainer.doResize();
		shellWidgetContainer.setParent(root);
		shellWidgetContainer.doReparent();
		shellWidgetContainer.doShow();

		final ShellNodeParent rootFrame = this.shellVirtualNodeProvider.get();
		final ShellLayoutManagerLine clientLayoutManagerLine = this.shellLayoutManagerLineProvider.get();
		rootFrame.setLayoutManager(clientLayoutManagerLine);
		rootFrame.setParent(root);
		rootFrame.doReparent();

		shellLayoutManagerLine.addChildNode(shellWidgetContainer,
											new ShellLayoutPropertyLine(0,
																		new Margins(0)));
		shellLayoutManagerLine.addChildNode(rootFrame,
											new ShellLayoutPropertyLine(1,
																		new Margins(0)));
		root.layout();

		new ShellNodeVisibilityMirror(	root,
										rootFrame);

		return rootFrame;
	}
}
/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.shellplugin.wm.x11.impl.scene;

import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.annotations.To;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.ExecutionContext;
import org.trinity.foundation.api.shared.Margins;
import org.trinity.shell.api.bindingkey.ShellExecutor;
import org.trinity.shell.api.bindingkey.ShellRootNode;
import org.trinity.shell.api.scene.ShellNodeParent;
import org.trinity.shell.api.scene.event.ShellNodeDestroyedEvent;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shellplugin.wm.api.Desktop;
import org.trinity.shellplugin.wm.x11.impl.protocol.XWindowProtocol;

import com.google.common.eventbus.Subscribe;

@Bind(to = @To(IMPLEMENTATION))
@NotThreadSafe
@Singleton
@ExecutionContext(ShellExecutor.class)
public class SceneManager {

	private final ClientBarElementFactory clientBarElementFactory;
	private final ShellLayoutManager rootLayoutManager;
	private final Desktop desktop;
	private final ShellNodeParent shellRootNode;
	private final XWindowProtocol xWindowProtocol;

	@Inject
	SceneManager(	final ClientBarElementFactory clientBarElementFactory,
					final Desktop desktop,
					final XWindowProtocol xWindowProtocol,
					@ShellRootNode final ShellNodeParent shellRootNode,
					final ShellLayoutManagerLine shellLayoutManagerLine) {
		this.clientBarElementFactory = clientBarElementFactory;
		this.desktop = desktop;
		this.xWindowProtocol = xWindowProtocol;
		this.shellRootNode = shellRootNode;
		this.rootLayoutManager = shellLayoutManagerLine;

		this.shellRootNode.setLayoutManager(this.rootLayoutManager);
		this.shellRootNode.doShow();
	}

	// called by shell executor
	public void manageNewClient(final DisplaySurface displaySurface,
								final ShellSurface client) {
		this.xWindowProtocol.register(displaySurface);
		addClientTopBarItem(client);
		layoutClient(client);
	}

	// called by shell executor
	private void addClientTopBarItem(final ShellSurface client) {
		final ClientBarElement clientBarElement = this.clientBarElementFactory.createClientTopBarItem(client);
		this.desktop.getClientsBar().add(clientBarElement);
		client.register(new Object() {
			// called by shell executor
			@Subscribe
			public void onClientDestroyed(final ShellNodeDestroyedEvent destroyedEvent) {
				SceneManager.this.desktop.getClientsBar().remove(clientBarElement);
			}
		});
	}

	private void layoutClient(final ShellSurface client) {
		client.setParent(this.shellRootNode);
		this.rootLayoutManager.addChildNode(client,
											new ShellLayoutPropertyLine(1,
																		new Margins(0,
																					20)));
		this.shellRootNode.layout();
		client.doReparent();
		client.doShow();
	}
}

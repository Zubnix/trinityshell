package org.trinity.shellplugin.wm.impl;

import org.trinity.foundation.api.shared.Margins;
import org.trinity.shell.api.scene.manager.ShellLayoutManager;
import org.trinity.shell.api.scene.manager.ShellLayoutManagerLine;
import org.trinity.shell.api.scene.manager.ShellLayoutPropertyLine;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.inject.Inject;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
public class Scene {

	private final ShellLayoutManager rootLayoutManager;
	private final ShellRootWidget shellRootWidget;

	@Inject
	Scene(	final ShellRootWidget shellRootWidget,
			final ShellLayoutManagerLine shellLayoutManagerLine) {
		this.shellRootWidget = shellRootWidget;
		this.rootLayoutManager = shellLayoutManagerLine;

		this.shellRootWidget.setLayoutManager(this.rootLayoutManager);
		this.shellRootWidget.doShow();
	}

	public void addClient(final ShellSurface client) {
		client.setParent(this.shellRootWidget);
		this.rootLayoutManager.addChildNode(client,
											new ShellLayoutPropertyLine(1,
																		new Margins(5)));
		this.shellRootWidget.layout();
	}
}

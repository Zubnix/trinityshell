package org.trinity.shellplugin.wm.impl;

import org.trinity.shell.api.scene.manager.ShellLayoutManager;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(to = @To(value = Type.IMPLEMENTATION))
public class Scene {

	private final ListeningExecutorService wmExecutor;
	private final ShellSurfaceFactory shellSurfaceFactory;
	private final ShellLayoutManager rootLayoutManager;
	private final ShellRootWidget shellRootWidget;

	@Inject
	Scene(	@Named("WmExecutor") final ListeningExecutorService wmExecutor,
			final ShellSurfaceFactory shellSurfaceFactory,
			final ShellRootWidget shellRootWidget) {
		this.wmExecutor = wmExecutor;
		this.shellSurfaceFactory = shellSurfaceFactory;
		this.shellRootWidget = shellRootWidget;

		this.shellRootWidget.setLayoutManager(this.rootLayoutManager);
	}

	public void addClient(final ShellSurface client) {
		client.setParent(this.shellRootWidget);
		this.rootLayoutManager.addChildNode(client,
											layoutProperty);
		this.shellRootWidget.layout();
	}
}

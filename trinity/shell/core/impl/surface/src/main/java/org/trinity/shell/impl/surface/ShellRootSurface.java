package org.trinity.shell.impl.surface;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.foundation.display.api.DisplaySurface;
import org.trinity.shell.api.geo.ShellNodeExecutor;
import org.trinity.shell.api.geo.manager.ShellLayoutManager;
import org.trinity.shell.api.surface.AbstractShellSurface;
import org.trinity.shell.api.surface.ShellDisplayEventDispatcher;
import org.trinity.shell.api.surface.ShellSurface;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;

@Bind(value = @Named("ShellRootSurface"), to = @To(value = To.Type.CUSTOM, customs = ShellSurface.class))
@Singleton
public class ShellRootSurface extends AbstractShellSurface {

	private final ShellNodeExecutor shellNodeExecutor;
	private final DisplaySurface displaySurface;

	@Inject
	ShellRootSurface(	final EventBus eventBus,
						@Named("shellSurfaceGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
						final DisplayServer displayServer,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcher) {
		super(eventBus);
		this.shellNodeExecutor = shellNodeExecutor;
		this.displaySurface = displayServer.getRootDisplayArea();
		syncGeoToDisplaySurface();
		shellDisplayEventDispatcher.registerDisplayEventSource(	eventBus,
																this.displaySurface);
	}

	@Override
	public int getAbsoluteX() {
		return getX();
	}

	@Override
	public int getAbsoluteY() {
		return getY();
	}

	@Override
	public ShellLayoutManager getParentLayoutManager() {
		return getLayoutManager();
	}

	@Override
	public ShellRootSurface getParent() {
		return this;
	}

	@Override
	public ShellNodeExecutor getNodeExecutor() {
		return this.shellNodeExecutor;
	}

	@Override
	public DisplaySurface getDisplaySurface() {
		return this.displaySurface;
	}
}
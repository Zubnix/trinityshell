package org.trinity.shell.core.impl;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.shell.core.api.AbstractShellSurface;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.ShellSurface;
import org.trinity.shell.geo.api.ShellNodeExecutor;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(value = @Named("shellRootRenderArea"), to = @To(value = Type.CUSTOM, customs = ShellSurface.class))
@Singleton
public class ShellRootSurface extends AbstractShellSurface {

	private final ShellNodeExecutor shellNodeExecutor;

	@Inject
	ShellRootSurface(	@Named("shellSurfaceGeoExecutor") final ShellNodeExecutor shellNodeExecutor,
						final DisplayServer displayServer,
						final EventBus nodeEventBus,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcher) {
		super(	nodeEventBus,
				shellDisplayEventDispatcher);
		this.shellNodeExecutor = shellNodeExecutor;
		setDisplaySurface(displayServer.getRootDisplayArea());
		syncGeoToDisplaySurface();
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
	public ShellRootSurface getParent() {
		return this;
	}

	@Override
	public ShellNodeExecutor getNodeExecutor() {
		return this.shellNodeExecutor;
	}
}
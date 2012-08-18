package org.trinity.shell.core.impl;

import org.trinity.foundation.display.api.DisplayServer;
import org.trinity.shell.core.api.AbstractShellRenderArea;
import org.trinity.shell.core.api.ShellDisplayEventDispatcher;
import org.trinity.shell.core.api.ShellRenderArea;
import org.trinity.shell.geo.api.ShellGeoExecutor;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Bind(value = @Named("shellRootRenderArea"), to = @To(value = Type.CUSTOM, customs = ShellRenderArea.class))
@Singleton
public class ShellRootRenderArea extends AbstractShellRenderArea {

	private final ShellGeoExecutor shellGeoExecutor;

	@Inject
	ShellRootRenderArea(@Named("shellRenderAreaGeoExecutor") final ShellGeoExecutor shellGeoExecutor,
						final DisplayServer displayServer,
						final EventBus nodeEventBus,
						final ShellDisplayEventDispatcher shellDisplayEventDispatcher) {
		super(nodeEventBus, shellDisplayEventDispatcher);
		this.shellGeoExecutor = shellGeoExecutor;
		setPlatformRenderArea(displayServer.getRootDisplayArea());
	}

	@Override
	public ShellGeoExecutor getGeoExecutor() {
		return this.shellGeoExecutor;
	}
}
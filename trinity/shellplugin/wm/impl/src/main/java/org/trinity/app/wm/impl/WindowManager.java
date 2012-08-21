package org.trinity.app.wm.impl;

import org.trinity.shell.core.api.ShellPlugin;
import org.trinity.shell.core.api.event.ShellSurfaceCreatedEvent;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class WindowManager implements ShellPlugin {

	private final EventBus shellEventBus;

	@Inject
	WindowManager(@Named("shellEventBus") final EventBus shellEventBus) {
		this.shellEventBus = shellEventBus;

	}

	@Subscribe
	public void handleShellClientCreated(final ShellSurfaceCreatedEvent shellSurfaceCreatedEvent) {
		// TODO create widget to move/resize/close client
	}

	@Override
	public void start() {
		// TODO ask for all known client shell surfaces & manage them
		this.shellEventBus.register(this);
	}

	@Override
	public void stop() {
		// TODO delete all created shell widgets
		this.shellEventBus.unregister(this);
	}
}

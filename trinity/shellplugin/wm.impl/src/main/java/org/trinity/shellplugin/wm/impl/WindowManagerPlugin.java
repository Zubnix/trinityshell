package org.trinity.shellplugin.wm.impl;

import org.trinity.shell.api.plugin.ShellPlugin;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.event.ShellSurfaceCreatedEvent;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class WindowManagerPlugin implements ShellPlugin {

	private final EventBus shellEventBus;

	private final DesktopImpl desktopImpl;

	@Inject
	WindowManagerPlugin(@Named("ShellEventBus") final EventBus shellEventBus, final DesktopImpl desktopImpl) {
		this.shellEventBus = shellEventBus;
		this.desktopImpl = desktopImpl;
	}

	@Override
	public void start() {
		this.desktopImpl.setup();
		this.shellEventBus.register(this);
	}

	@Override
	public void stop() {
		this.shellEventBus.unregister(this);
		this.desktopImpl.tearDown();
	}

	@Subscribe
	public void handleShellSurfaceCreated(final ShellSurfaceCreatedEvent shellSurfaceCreatedEvent) {
		final ShellSurface shellSurface = shellSurfaceCreatedEvent.getClient();
		this.desktopImpl.addClient(shellSurface);
	}
}

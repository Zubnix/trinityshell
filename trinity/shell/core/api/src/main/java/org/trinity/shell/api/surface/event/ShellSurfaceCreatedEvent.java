package org.trinity.shell.api.surface.event;

import org.trinity.shell.api.surface.ShellSurface;

public class ShellSurfaceCreatedEvent {

	private final ShellSurface client;

	public ShellSurfaceCreatedEvent(final ShellSurface client) {
		this.client = client;
	}

	public ShellSurface getClient() {
		return this.client;
	}
}

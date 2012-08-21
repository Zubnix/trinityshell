package org.trinity.shell.core.api.event;

import org.trinity.shell.core.api.ShellSurface;

public class ShellSurfaceCreatedEvent {

	private final ShellSurface client;

	public ShellSurfaceCreatedEvent(final ShellSurface client) {
		this.client = client;
	}

	public ShellSurface getClient() {
		return this.client;
	}
}

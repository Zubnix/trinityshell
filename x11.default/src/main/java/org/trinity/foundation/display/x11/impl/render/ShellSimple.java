package org.trinity.foundation.display.x11.impl.render;

import org.trinity.foundation.display.x11.impl.XWindow;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShellSimple {

	private final ShellSurfaceRoot shellSurfaceRoot;

	@Inject
	ShellSimple(final ShellSurfaceRoot shellSurfaceRoot) {
		this.shellSurfaceRoot = shellSurfaceRoot;
	}

	public void addClient(final XWindow xWindow) {

	}
}

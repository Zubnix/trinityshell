package org.trinity.x11.defaul.shell;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;

@Singleton
public class SimpleShellScene {

	private final LinkedList<SimpleShellSurface> shellSurfaces = new LinkedList<>();

	@Inject
	SimpleShellScene(final SimpleRootShellSurface rootShellSurface) {
		this.shellSurfaces.add(rootShellSurface);
	}


	public void add(final SimpleShellSurface shellSurface) {
		this.shellSurfaces.add(shellSurface);
	}
}

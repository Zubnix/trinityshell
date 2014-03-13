package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;

public interface ShellSurfaceFactory {

	ShellSurface construct(@Nonnull ShellSurface parent,
						   @Nonnull HasSize<SpaceBuffer> buffer);
}

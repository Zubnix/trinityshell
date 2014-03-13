package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;

public interface ShellNodeFactory {

	ShellSurface construct(@Nonnull ShellSurface parent, @Nonnull Object buffer);
}

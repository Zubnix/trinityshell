package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;

/**
 *
 */
public interface ShellSurfaceConfiguration {
    void visit(@Nonnull ShellSurfaceConfigurable shellSurfaceConfigurable);
}

package org.trinity.shell.scene.api.event;

import org.trinity.shell.scene.api.ShellSurface;

import javax.annotation.Nonnull;

public class Committed extends ShellSurfaceEvent {

    public Committed(@Nonnull final ShellSurface shellSurface) {
        super(shellSurface);
    }
}

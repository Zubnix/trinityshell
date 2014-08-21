package org.trinity.wayland;

import org.freedesktop.wayland.server.ShmBuffer;
import org.trinity.shell.scene.api.ShellSurface;

/**
 * Created by Erik De Rijcke on 6/10/14.
 */
public interface WlShmRenderEngine {
    void draw(final ShellSurface shellSurface,
              final ShmBuffer buffer);
}

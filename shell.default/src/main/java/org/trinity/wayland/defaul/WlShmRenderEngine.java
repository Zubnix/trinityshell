package org.trinity.wayland.defaul;

import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.defaul.protocol.WlShmBuffer;

/**
 * Created by Erik De Rijcke on 6/10/14.
 */
public interface WlShmRenderEngine {
    void draw(final ShellSurface shellSurface,
              final WlShmBuffer buffer);
}

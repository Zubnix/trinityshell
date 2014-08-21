package org.trinity.wayland.render.fb;

import org.freedesktop.wayland.server.ShmBuffer;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.WlShmRenderEngine;

import javax.inject.Inject;

/**
 * Created by Erik De Rijcke on 6/10/14.
 */
public class FBRenderEngine implements WlShmRenderEngine {

    @Inject
    FBRenderEngine() {
    }

    @Override
    public void draw(final ShellSurface shellSurface,
                     final ShmBuffer buffer) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}

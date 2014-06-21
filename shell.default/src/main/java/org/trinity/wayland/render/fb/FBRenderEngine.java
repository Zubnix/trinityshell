package org.trinity.wayland.render.fb;

import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.WlShmRenderEngine;
import org.trinity.wayland.protocol.WlShmBuffer;

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
                     final WlShmBuffer buffer) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}

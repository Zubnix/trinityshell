package org.trinity.wayland;

import com.google.common.util.concurrent.ListenableFuture;
import org.freedesktop.wayland.server.ShmBuffer;
import org.trinity.shell.scene.api.ShellSurface;

public interface WlShmRenderEngine {
    ListenableFuture<?> draw(final ShellSurface shellSurface,
                             final ShmBuffer buffer);

    ListenableFuture<?> begin();

    ListenableFuture<?> end();
}

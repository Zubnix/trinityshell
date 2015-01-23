package org.trinity.wayland.output;

import com.google.common.util.concurrent.ListenableFuture;
import org.freedesktop.wayland.server.ShmBuffer;
import org.freedesktop.wayland.server.WlSurfaceResource;

public interface ShmRenderEngine {
    ListenableFuture<?> draw(final WlSurfaceResource surfaceResource,
                             final ShmBuffer buffer);

    ListenableFuture<?> begin();

    ListenableFuture<?> end();
}

package org.trinity.wayland.output;


import org.freedesktop.wayland.server.WlBufferResource;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.nativewindow.util.RectangleImmutable;
import java.util.function.IntConsumer;

public interface SurfaceConfigurable {
    @Nonnull
    SurfaceConfigurable addCallback(IntConsumer callback);

    @Nonnull
    SurfaceConfigurable removeOpaqueRegion();

    @Nonnull
    SurfaceConfigurable setOpaqueRegion(@Nonnull Region opaqueRegion);

    @Nonnull
    SurfaceConfigurable removeInputRegion();

    @Nonnull
    SurfaceConfigurable setInputRegion(@Nonnull Region inputRegion);

    @Nonnull
    SurfaceConfigurable setPosition(@Nonnull PointImmutable position);

    @Nonnull
    SurfaceConfigurable markDestroyed();

    @Nonnull
    SurfaceConfigurable markDamaged(@Nonnull RectangleImmutable damage);

    @Nonnull
    SurfaceConfigurable attachBuffer(@Nonnull WlBufferResource buffer,
                                     @Nonnull Integer relX,
                                     @Nonnull Integer relY);

    @Nonnull
    SurfaceConfigurable setTransform(float[] transform);

    @Nonnull
    SurfaceConfigurable removeTransform();

    @Nonnull
    SurfaceConfigurable detachBuffer();

    @Nonnull
    SurfaceConfigurable commit();
}

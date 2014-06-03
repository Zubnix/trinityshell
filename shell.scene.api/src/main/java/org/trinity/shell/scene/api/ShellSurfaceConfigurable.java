package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.nativewindow.util.RectangleImmutable;
import java.util.function.IntConsumer;

/**
 *
 */
public interface ShellSurfaceConfigurable {

    @Nonnull
    ShellSurfaceConfigurable addPaintCallback(IntConsumer callback);
    @Nonnull
    ShellSurfaceConfigurable removeOpaqueRegion();
    @Nonnull
    ShellSurfaceConfigurable setOpaqueRegion(@Nonnull Region opaqueRegion);
    @Nonnull
    ShellSurfaceConfigurable removeInputRegion();
    @Nonnull
    ShellSurfaceConfigurable setInputRegion(@Nonnull Region inputRegion);
    @Nonnull
    ShellSurfaceConfigurable setPosition(@Nonnull PointImmutable position);
    @Nonnull
    ShellSurfaceConfigurable markDestroyed();
    @Nonnull
    ShellSurfaceConfigurable markDamaged(@Nonnull RectangleImmutable damage);
    @Nonnull
    ShellSurfaceConfigurable attachBuffer(@Nonnull Buffer buffer,
                                          @Nonnull Integer relX,
                                          @Nonnull Integer relY);
    @Nonnull
    ShellSurfaceConfigurable detachBuffer();
    @Nonnull
    ShellSurfaceConfigurable commit();
}

package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.nativewindow.util.RectangleImmutable;

/**
 *
 */
public interface ShellSurfaceConfigurable {

    @Nonnull
    ShellSurfaceConfigurable setPosition(@Nonnull PointImmutable position);

    @Nonnull
    ShellSurfaceConfigurable markDestroyed();

    @Nonnull
    ShellSurfaceConfigurable markDamaged(@Nonnull RectangleImmutable damage);

    @Nonnull
    ShellSurfaceConfigurable attachBuffer(@Nonnull HasSize<BufferSpace> buffer,
                                          @Nonnull Integer relX,
                                          @Nonnull Integer relY);

    @Nonnull
    ShellSurfaceConfigurable detachBuffer();

    @Nonnull
    ShellSurfaceConfigurable commit();
}

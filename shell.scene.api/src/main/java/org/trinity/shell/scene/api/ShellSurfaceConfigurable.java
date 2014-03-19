package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.PointImmutable;

/**
 *
 */
public interface ShellSurfaceConfigurable {

    @Nonnull
    ShellSurfaceConfigurable setPosition(@Nonnull PointImmutable pointImmutable);

    @Nonnull
    ShellSurfaceConfigurable attachBuffer(@Nonnull HasSize<BufferSpace> buffer);

    @Nonnull
    ShellSurfaceConfigurable setParent(@Nonnull ShellSurface shellSurface);

    @Nonnull
    ShellSurfaceConfigurable markDestroyed();

    @Nonnull
    ShellSurfaceConfigurable setVisible(@Nonnull Boolean visible);

    @Nonnull
    ShellSurfaceConfigurable commit();
}

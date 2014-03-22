package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.PointImmutable;

/**
 *
 */
public interface ShellSurfaceConfigurable {

    @Nonnull
    ShellSurfaceConfigurable setPosition(@Nonnull PointImmutable pointImmutable);

    @Nonnull
    ShellSurfaceConfigurable markDestroyed();

	@Nonnull
	ShellSurfaceConfigurable commit();

    @Nonnull
    ShellSurfaceConfigurable setVisible(@Nonnull Boolean visible);
}

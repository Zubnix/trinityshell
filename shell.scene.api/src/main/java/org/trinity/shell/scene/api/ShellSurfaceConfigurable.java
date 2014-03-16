package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.PointImmutable;

/**
 *
 */
public interface ShellSurfaceConfigurable {

    void setPosition(@Nonnull PointImmutable pointImmutable);

    void setSize(@Nonnull DimensionImmutable size);

	void attachBuffer(@Nonnull HasSize<BufferSpace> buffer);

	void setParent(@Nonnull ShellSurface shellSurface);

	void markDestroyed();

    void setVisible(@Nonnull Boolean visible);
}

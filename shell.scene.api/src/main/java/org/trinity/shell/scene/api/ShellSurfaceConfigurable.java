package org.trinity.shell.scene.api;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 */
public interface ShellSurfaceConfigurable {

	void setShape(int x,
                  int y,
                  @Nonnegative int width,
                  @Nonnegative int height);

	void attachBuffer(@Nonnull HasSize<SpaceBuffer> buffer);

	void setParent(@Nonnull ShellSurface shellSurface);

	void markDestroyed();

    void setVisible(Boolean visible);
}

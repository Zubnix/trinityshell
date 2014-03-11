package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.DimensionImmutable;

/**
 *
 */
public interface ShellSurfaceConfigurable extends ShellNodeConfigurable{

    void setDisplayBuffer(@Nonnull Object buffer);

    void setMaxSize(@Nonnull final DimensionImmutable size);

    void setMinSize(@Nonnull final DimensionImmutable size);
}

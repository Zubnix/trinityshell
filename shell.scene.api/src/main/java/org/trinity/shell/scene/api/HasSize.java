package org.trinity.shell.scene.api;

import javax.annotation.Nonnull;
import javax.media.nativewindow.util.DimensionImmutable;

public interface HasSize<T extends Space> {
    @Nonnull
	DimensionImmutable getSize();
}
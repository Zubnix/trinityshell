package org.trinity.shell.scene.api;

import javax.media.nativewindow.util.DimensionImmutable;

public interface HasSize<T extends Space> {
	DimensionImmutable getSize();
}
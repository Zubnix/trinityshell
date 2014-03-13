package org.trinity.shell.scene.api;

import javax.media.nativewindow.util.DimensionImmutable;

/**
 * Created by zubzub on 12/03/14.
 */
public interface HasSize<T extends Space> {
    DimensionImmutable getSize();
}
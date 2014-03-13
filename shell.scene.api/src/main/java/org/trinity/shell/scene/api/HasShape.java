package org.trinity.shell.scene.api;

import javax.media.nativewindow.util.RectangleImmutable;

/**
 * Created by zubzub on 13/03/14.
 */
public interface HasShape<T extends Space> {
    RectangleImmutable getShape();
}

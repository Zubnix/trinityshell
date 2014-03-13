package org.trinity.shell.scene.api;

import javax.media.nativewindow.util.RectangleImmutable;

public interface HasShape<T extends Space> {
    RectangleImmutable getShape();
}

package org.trinity.shell.scene.api;

import javax.media.nativewindow.util.RectangleImmutable;
import java.util.List;

public interface Region  {
    List<RectangleImmutable> asList();

    Region add(RectangleImmutable rectangle);

    Region subtract(RectangleImmutable rectangle);
}

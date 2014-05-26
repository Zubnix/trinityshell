package org.trinity.wayland.defaul;

import org.trinity.wayland.defaul.protocol.WlShmBuffer;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
public interface WlShmBufferRenderer {
    void visit(WlShmBuffer wlShmBuffer);
}

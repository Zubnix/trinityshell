package org.trinity.x11.defaul;

import org.trinity.common.Listenable;

/**
 *
 */
public interface XCompositor {
    Listenable createSurface(Integer nativeHandle);
}

package org.trinity.foundation.display.x11.impl;

import org.trinity.common.Listenable;

/**
 *
 */
public interface XCompositor {
    Listenable createSurface(Integer nativeHandle);
}

package org.trinity.foundation.display.x11.api;


import org.trinity.foundation.api.display.DisplaySurface;

/**
 *
 */
public interface XWindowCache {
    DisplaySurface getWindow(final int windowId);

    boolean isPresent(final int windowId);
}

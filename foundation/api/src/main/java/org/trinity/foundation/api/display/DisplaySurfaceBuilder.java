package org.trinity.foundation.api.display;

/**
 *
 */
public interface DisplaySurfaceBuilder extends AutoCloseable{
    void build(DisplaySurfaceHandle displaySurfaceHandle);
}

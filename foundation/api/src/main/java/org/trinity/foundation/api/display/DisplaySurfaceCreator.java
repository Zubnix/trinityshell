package org.trinity.foundation.api.display;

/**
 *
 */
public interface DisplaySurfaceCreator extends AutoCloseable{
    DisplaySurface reference(DisplaySurfaceHandle displaySurfaceHandle);

    @Override
    void close();
}

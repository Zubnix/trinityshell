package org.trinity.foundation.api.display;

/**
 *
 */
public interface DisplaySurfaceCreator extends AutoCloseable{
    DisplaySurface create(DisplaySurfaceHandle displaySurfaceHandle);

    @Override
    void close();
}

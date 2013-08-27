package org.trinity.foundation.api.display;

/**
 *
 */
public interface DisplaySurfaceCreator extends AutoCloseable{
    void create(DisplaySurfaceHandle displaySurfaceHandle);

    @Override
    void close();
}

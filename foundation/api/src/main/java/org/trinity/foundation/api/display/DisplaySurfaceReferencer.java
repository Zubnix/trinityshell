package org.trinity.foundation.api.display;

/**
 *
 */
public interface DisplaySurfaceReferencer extends AutoCloseable {
    DisplaySurface reference(DisplaySurfaceHandle displaySurfaceHandle);

    @Override
    void close();
}

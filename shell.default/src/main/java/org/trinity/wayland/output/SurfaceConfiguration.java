package org.trinity.wayland.output;


import javax.annotation.Nonnull;

public interface SurfaceConfiguration {
    void visit(@Nonnull SurfaceConfigurable shellSurfaceConfigurable);
}

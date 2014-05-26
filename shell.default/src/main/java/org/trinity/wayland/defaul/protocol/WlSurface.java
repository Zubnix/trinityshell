package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_surface;
import org.freedesktop.wayland.server.Resource;
import org.trinity.wayland.defaul.protocol.events.ResourceDestroyed;

import javax.annotation.Nonnegative;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory
public class WlSurface extends EventBus implements wl_surface.Requests3 {

    WlSurface() {
    }

    @Override
    public void setBufferScale(final wl_surface.Resource    resource,
                               final int                    scale) {

    }

    @Override
    public void setBufferTransform(final wl_surface.Resource    resource,
                                   final int                    transform) {

    }

    @Override
    public void destroy(final wl_surface.Resource resource) {
        post(new ResourceDestroyed(resource));
    }

    @Override
    public void attach(final wl_surface.Resource    resource,
                       final Resource               buffer,
                       final int                    x,
                       final int                    y) {
        final WlShmBuffer wlShmBuffer = (WlShmBuffer) buffer.getImplementation();
    }

    @Override
    public void damage(final wl_surface.Resource    resource,
                       final int                    x,
                       final int                    y,
                       @Nonnegative final int       width,
                       @Nonnegative final int       height) {
        checkArgument(width > 0);
        checkArgument(height > 0);

    }

    @Override
    public void frame(final wl_surface.Resource resource,
                      final int                 callback) {

    }

    @Override
    public void setOpaqueRegion(final wl_surface.Resource resource,
                                final Resource            region) {
        final WlRegion wlRegion = (WlRegion) region.getImplementation();

    }

    @Override
    public void setInputRegion(final wl_surface.Resource resource,
                               final Resource            region) {
        final WlRegion wlRegion = (WlRegion) region.getImplementation();

    }

    @Override
    public void commit(final wl_surface.Resource resource) {

    }
}

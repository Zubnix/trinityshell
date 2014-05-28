package org.trinity.wayland.defaul.protocol;

import org.freedesktop.wayland.protocol.wl_compositor;
import org.freedesktop.wayland.protocol.wl_region;
import org.freedesktop.wayland.protocol.wl_surface;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.trinity.SimpleShellSurfaceFactory;
import org.trinity.shell.scene.api.Buffer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//Eager
public class WlCompositor extends Global implements wl_compositor.Requests {

    private final WlSurfaceFactory          wlSurfaceFactory;
    private final WlRegionFactory           wlRegionFactory;
    private final SimpleShellSurfaceFactory simpleShellSurfaceFactory;

    @Inject
    WlCompositor(final Display                   display,
                 final WlSurfaceFactory          wlSurfaceFactory,
                 final WlRegionFactory           wlRegionFactory,
                 final SimpleShellSurfaceFactory simpleShellSurfaceFactory) {
        super(display,
              wl_compositor.WAYLAND_INTERFACE,
              1);
        this.wlSurfaceFactory           = wlSurfaceFactory;
        this.wlRegionFactory            = wlRegionFactory;
        this.simpleShellSurfaceFactory  = simpleShellSurfaceFactory;
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id) {
        new wl_compositor.Resource(client,
                                   version,
                                   id).setImplementation(this);
    }

    @Override
    public void createSurface(final wl_compositor.Resource  resource,
                              final int                     id) {
        new wl_surface.Resource(resource.getClient(),
                                1,
                                id).setImplementation(this.wlSurfaceFactory.create(this.simpleShellSurfaceFactory.create(Optional.<Buffer>empty())));
    }

    @Override
    public void createRegion(final wl_compositor.Resource   resource,
                             final int                      id) {
        new wl_region.Resource(resource.getClient(),
                               1,
                               id).setImplementation(this.wlRegionFactory.create());
    }
}

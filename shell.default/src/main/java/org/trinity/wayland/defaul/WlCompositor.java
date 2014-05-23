package org.trinity.wayland.defaul;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.protocol.wl_compositor;
import org.freedesktop.wayland.protocol.wl_region;
import org.freedesktop.wayland.protocol.wl_surface;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.trinity.wayland.defaul.events.ResourceDestroyed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//Eager
public class WlCompositor extends Global implements wl_compositor.Requests {

    private final WlSurfaceFactory  wlSurfaceFactory;
    private final WlRegionFactory   wlRegionFactory;

    @Inject
    WlCompositor(final Display          display,
                 final WlSurfaceFactory wlSurfaceFactory,
                 final WlRegionFactory  wlRegionFactory ) {
        super(display,
              wl_compositor.WAYLAND_INTERFACE,
              1);
        this.wlSurfaceFactory   = wlSurfaceFactory;
        this.wlRegionFactory    = wlRegionFactory;
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
        final WlSurface wlSurface                   = this.wlSurfaceFactory.create();
        final wl_surface.Resource surfaceResource   = new wl_surface.Resource(resource.getClient(),
                                                                              1,
                                                                              id);
        surfaceResource.setImplementation(wlSurface);
        wlSurface.register(new Object(){
            @Subscribe
            public void handle(final ResourceDestroyed event){
                wlSurface.unregister(this);
                surfaceResource.destroy();
            }
        });
    }

    @Override
    public void createRegion(final wl_compositor.Resource   resource,
                             final int                      id) {
        final WlRegion wlRegion                 = this.wlRegionFactory.create();
        final wl_region.Resource regionResource = new wl_region.Resource(resource.getClient(),
                                                                         1,
                                                                         id);
        regionResource.setImplementation(wlRegion);
        wlRegion.register(new Object(){
            @Subscribe
            public void handle(final ResourceDestroyed event){
                wlRegion.unregister(this);
                regionResource.destroy();
            }
        });
    }
}

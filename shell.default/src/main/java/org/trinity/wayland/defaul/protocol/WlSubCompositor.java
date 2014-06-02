package org.trinity.wayland.defaul.protocol;

import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_subcompositor;
import org.freedesktop.wayland.protocol.wl_subsurface;
import org.freedesktop.wayland.server.Resource;
import org.trinity.wayland.defaul.events.ResourceDestroyed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 6/2/14.
 */
@Singleton
public class WlSubCompositor extends EventBus implements wl_subcompositor.Requests {

    private final WlSubSurfaceFactory wlSubSurfaceFactory;

    @Inject
    WlSubCompositor(final WlSubSurfaceFactory wlSubSurfaceFactory) {
        this.wlSubSurfaceFactory = wlSubSurfaceFactory;
    }

    @Override
    public void destroy(final wl_subcompositor.Resource resource) {
        post(new ResourceDestroyed(resource));
        resource.destroy();
    }

    @Override
    public void getSubsurface(final wl_subcompositor.Resource resource,
                              final int                       id,
                              final Resource                  surfaceResource,
                              final Resource                  parentResource) {
        new wl_subsurface.Resource(resource.getClient(),
                                   1,
                                   id).setImplementation(this.wlSubSurfaceFactory.create());
    }
}

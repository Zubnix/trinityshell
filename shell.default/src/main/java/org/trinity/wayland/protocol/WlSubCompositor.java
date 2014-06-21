package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_subcompositor;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/2/14.
 */
@Singleton
public class WlSubCompositor extends EventBus implements wl_subcompositor.Requests, ProtocolObject<wl_subcompositor.Resource> {

    private final Set<wl_subcompositor.Resource> resources = Sets.newHashSet();
    private final WlSubSurfaceFactory wlSubSurfaceFactory;

    @Inject
    WlSubCompositor(final WlSubSurfaceFactory wlSubSurfaceFactory) {
        this.wlSubSurfaceFactory = wlSubSurfaceFactory;
    }

    @Override
    public void destroy(final wl_subcompositor.Resource resource) {
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void getSubsurface(final wl_subcompositor.Resource resource,
                              final int                       id,
                              final Resource                  surfaceResource,
                              final Resource                  parentResource) {
        this.wlSubSurfaceFactory.create().add(resource.getClient(),
                                              1,
                                              id);
    }

    @Override
    public Set<wl_subcompositor.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_subcompositor.Resource create(final Client client,
                                            final int version,
                                            final int id) {
        return new wl_subcompositor.Resource(client,
                                             version,
                                             id);
    }
}

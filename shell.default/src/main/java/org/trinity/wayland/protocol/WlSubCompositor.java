package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlSubcompositorRequests;
import org.freedesktop.wayland.server.WlSubcompositorResource;
import org.freedesktop.wayland.server.WlSurfaceResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/2/14.
 */
@Singleton
public class WlSubCompositor extends EventBus implements WlSubcompositorRequests, ProtocolObject<WlSubcompositorResource> {

    private final Set<WlSubcompositorResource> resources = Sets.newHashSet();
    private final WlSubSurfaceFactory wlSubSurfaceFactory;

    @Inject
    WlSubCompositor(final WlSubSurfaceFactory wlSubSurfaceFactory) {
        this.wlSubSurfaceFactory = wlSubSurfaceFactory;
    }

    @Override
    public void destroy(final WlSubcompositorResource resource) {
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void getSubsurface(final WlSubcompositorResource requester,
                              final int id,
                              final WlSurfaceResource surface,
                              final WlSurfaceResource parent) {
        this.wlSubSurfaceFactory.create(surface,
                                        parent).add(requester.getClient(),
                                                    requester.getVersion(),
                                                    id);
    }

    @Override
    public Set<WlSubcompositorResource> getResources() {
        return this.resources;
    }

    @Override
    public WlSubcompositorResource create(final Client client,
                                            final int version,
                                            final int id) {
        return new WlSubcompositorResource(client,
                                           version,
                                           id,
                                           this);
    }
}

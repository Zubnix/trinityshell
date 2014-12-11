package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlSubsurfaceRequests;
import org.freedesktop.wayland.server.WlSubsurfaceResource;
import org.freedesktop.wayland.server.WlSurfaceResource;

import java.util.Set;

@AutoFactory(className = "WlSubSurfaceFactory")
public class WlSubSurface extends EventBus implements WlSubsurfaceRequests, ProtocolObject<WlSubsurfaceResource> {

    private final Set<WlSubsurfaceResource> resources = Sets.newHashSet();

    WlSubSurface(final WlSurfaceResource surface,
                 final WlSurfaceResource parent) {
    }

    @Override
    public Set<WlSubsurfaceResource> getResources() {
        return this.resources;
    }

    @Override
    public WlSubsurfaceResource create(final Client client,
                                       final int version,
                                       final int id) {
        return new WlSubsurfaceResource(client,
                                        version,
                                        id,
                                        this);
    }

    @Override
    public void destroy(final WlSubsurfaceResource resource) {
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void setPosition(final WlSubsurfaceResource requester,
                            final int x,
                            final int y) {

    }

    @Override
    public void placeAbove(final WlSubsurfaceResource requester,
                           final WlSurfaceResource sibling) {

    }

    @Override
    public void placeBelow(final WlSubsurfaceResource requester,
                           final WlSurfaceResource sibling) {

    }

    @Override
    public void setSync(final WlSubsurfaceResource requester) {

    }

    @Override
    public void setDesync(final WlSubsurfaceResource requester) {

    }
}

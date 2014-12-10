package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlPointerRequestsV3;
import org.freedesktop.wayland.server.WlPointerResource;
import org.freedesktop.wayland.server.WlSurfaceResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@AutoFactory
public class WlPointer extends EventBus implements WlPointerRequestsV3, ProtocolObject<WlPointerResource> {

    private final Set<WlPointerResource> resources = Sets.newHashSet();

    @Inject
    WlPointer(){
    }

    @Override
    public void release(final WlPointerResource resource) {

    }

    @Override
    public void setCursor(final WlPointerResource requester,
                          final int serial,
                          final WlSurfaceResource surface,
                          final int hotspotX,
                          final int hotspotY) {

    }

    @Override
    public Set<WlPointerResource> getResources() {
        return this.resources;
    }

    @Override
    public WlPointerResource create(final Client client,
                                      final int version,
                                      final int id) {
        return new WlPointerResource(client,
                                     version,
                                     id,
                                     this);
    }
}

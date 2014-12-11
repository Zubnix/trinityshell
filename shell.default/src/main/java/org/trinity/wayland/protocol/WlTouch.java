package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlTouchRequestsV3;
import org.freedesktop.wayland.server.WlTouchResource;

import javax.inject.Inject;
import java.util.Set;

@AutoFactory
public class WlTouch extends EventBus implements WlTouchRequestsV3, ProtocolObject<WlTouchResource> {

    private final Set<WlTouchResource> resources = Sets.newHashSet();

    @Inject
    WlTouch() {
    }

    @Override
    public void release(final WlTouchResource resource) {

    }

    @Override
    public Set<WlTouchResource> getResources() {
        return this.resources;
    }

    @Override
    public WlTouchResource create(final Client client,
                                  final int version,
                                  final int id) {
        return new WlTouchResource(client,
                                   version,
                                   id,
                                   this);
    }
}

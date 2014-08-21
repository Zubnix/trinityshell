package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlTouchRequestsV3;
import org.freedesktop.wayland.server.WlTouchResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Singleton
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

package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlKeyboardRequestsV3;
import org.freedesktop.wayland.server.WlKeyboardResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@AutoFactory
public class WlKeyboard extends EventBus implements WlKeyboardRequestsV3, ProtocolObject<WlKeyboardResource> {

    private final Set<WlKeyboardResource> resources = Sets.newHashSet();

    @Inject
    WlKeyboard() {
    }

    @Override
    public void release(final WlKeyboardResource resource) {

    }

    @Override
    public Set<WlKeyboardResource> getResources() {
        return this.resources;
    }

    @Override
    public WlKeyboardResource create(final Client client,
                                       final int version,
                                       final int id) {
        return new WlKeyboardResource(client,
                                      version,
                                      id,
                                      this);
    }
}

package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlCallbackRequests;
import org.freedesktop.wayland.server.WlCallbackResource;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/3/14.
 */
@AutoFactory(className = "WlCallbackFactory")
public class WlCallback extends EventBus implements WlCallbackRequests, ProtocolObject<WlCallbackResource> {

    private final Set<WlCallbackResource> resources = Sets.newHashSet();

    @Inject
    WlCallback() {
    }

    @Override
    public Set<WlCallbackResource> getResources() {
        return this.resources;
    }

    @Override
    public WlCallbackResource create(final Client client,
                                       final int version,
                                       final int id) {
        return new WlCallbackResource(client,
                                      version,
                                      id,
                                      this);
    }


}

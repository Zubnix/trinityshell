package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_callback;
import org.freedesktop.wayland.server.Client;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/3/14.
 */
@AutoFactory(className = "WlCallbackFactory")
public class WlCallback extends EventBus implements wl_callback.Requests, ProtocolObject<wl_callback.Resource> {

    private final Set<wl_callback.Resource> resources = Sets.newHashSet();

    @Inject
    WlCallback() {
    }

    @Override
    public Set<wl_callback.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_callback.Resource create(final Client client,
                                       final int version,
                                       final int id) {
        return new wl_callback.Resource(client,
                                        version,
                                        id);
    }
}

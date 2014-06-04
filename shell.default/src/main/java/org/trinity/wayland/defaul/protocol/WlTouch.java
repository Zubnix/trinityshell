package org.trinity.wayland.defaul.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_touch;
import org.freedesktop.wayland.server.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Singleton
public class WlTouch extends EventBus implements wl_touch.Requests3, ProtocolObject<wl_touch.Resource> {

    private final Set<wl_touch.Resource> resources = Sets.newHashSet();

    @Inject
    WlTouch() {
    }

    @Override
    public void release(final wl_touch.Resource resource) {

    }

    @Override
    public Set<wl_touch.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_touch.Resource create(final Client client,
                                    final int version,
                                    final int id) {
        return new wl_touch.Resource(client,
                                     version,
                                     id);
    }
}

package org.trinity.wayland.defaul.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_pointer;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Singleton
public class WlPointer extends EventBus implements wl_pointer.Requests3, ProtocolObject<wl_pointer.Resource> {

    private final Set<wl_pointer.Resource> resources = Sets.newHashSet();

    @Inject
    WlPointer(){
    }

    @Override
    public void release(final wl_pointer.Resource resource) {

    }

    @Override
    public void setCursor(final wl_pointer.Resource resource,
                          final int serial,
                          final Resource surfaceResource,
                          final int hotspotX,
                          final int hotspotY) {

    }

    @Override
    public Set<wl_pointer.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_pointer.Resource create(final Client client,
                                      final int version,
                                      final int id) {
        return new wl_pointer.Resource(client,
                                       version,
                                       id);
    }
}

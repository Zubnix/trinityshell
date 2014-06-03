package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_subsurface;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;

import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/2/14.
 */
@AutoFactory(className = "WlSubSurfaceFactory")
public class WlSubSurface extends EventBus implements wl_subsurface.Requests, ProtocolObject<wl_subsurface.Resource> {

    private final Set<wl_subsurface.Resource> resources = Sets.newHashSet();

    WlSubSurface() {
    }

    @Override
    public Set<wl_subsurface.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_subsurface.Resource create(final Client client,
                                         final int version,
                                         final int id) {
        return new wl_subsurface.Resource(client,
                                          version,
                                          id);
    }

    @Override
    public void destroy(final wl_subsurface.Resource resource) {
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void setPosition(final wl_subsurface.Resource resource,
                            final int                    x,
                            final int                    y) {

    }

    @Override
    public void placeAbove(final wl_subsurface.Resource resource,
                           final Resource               sibling) {

    }

    @Override
    public void placeBelow(final wl_subsurface.Resource resource,
                           final Resource               sibling) {

    }

    @Override
    public void setSync(final wl_subsurface.Resource resource) {

    }

    @Override
    public void setDesync(final wl_subsurface.Resource resource) {

    }
}

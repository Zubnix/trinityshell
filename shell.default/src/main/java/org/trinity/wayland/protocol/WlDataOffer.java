package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlDataOfferRequests;
import org.freedesktop.wayland.server.WlDataOfferResource;

import java.util.Set;


/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@AutoFactory(className = "WlDataOfferFactory")
public class WlDataOffer extends EventBus implements WlDataOfferRequests, ProtocolObject<WlDataOfferResource> {

    private final Set<WlDataOfferResource> resources = Sets.newHashSet();

    WlDataOffer(){
    }

    @Override
    public void accept(final WlDataOfferResource resource,
                       final int                 serial,
                       final String              mimeType) {

    }

    @Override
    public void receive(final WlDataOfferResource resource,
                        final String              mimeType,
                        final int                 fd) {

    }

    @Override
    public Set<WlDataOfferResource> getResources() {
        return this.resources;
    }

    @Override
    public WlDataOfferResource create(final Client client,
                                         final int    version,
                                         final int    id) {
        return new WlDataOfferResource(client,
                                       version,
                                       id,
                                       this);
    }

    @Override
    public void destroy(final WlDataOfferResource resource) {
        ProtocolObject.super.destroy(resource);
    }
}

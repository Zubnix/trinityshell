package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_data_offer;
import org.freedesktop.wayland.server.Client;

import java.util.Set;


/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@AutoFactory(className = "WlDataOfferFactory")
public class WlDataOffer extends EventBus implements wl_data_offer.Requests, ProtocolObject<wl_data_offer.Resource> {

    private final Set<wl_data_offer.Resource> resources = Sets.newHashSet();

    WlDataOffer(){
    }

    @Override
    public void accept(final wl_data_offer.Resource resource,
                       final int serial,
                       final String mimeType) {

    }

    @Override
    public void receive(final wl_data_offer.Resource resource,
                        final String mimeType,
                        final int fd) {

    }

    @Override
    public Set<wl_data_offer.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_data_offer.Resource create(Client client,
                                         int version,
                                         int id) {
        return new wl_data_offer.Resource(client,
                                          version,
                                          id);
    }

    @Override
    public void destroy(final wl_data_offer.Resource resource) {
        ProtocolObject.super.destroy(resource);
    }
}

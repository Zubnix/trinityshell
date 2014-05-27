package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_data_offer;
import org.trinity.common.Listenable;
import org.trinity.wayland.defaul.protocol.events.ResourceDestroyed;


/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@AutoFactory
public class WlDataOffer extends EventBus implements wl_data_offer.Requests, Listenable {

    WlDataOffer() {
    }

    @Override
    public void accept(final wl_data_offer.Resource resource,
                       final int                    serial,
                       final String                 mimeType) {

    }

    @Override
    public void receive(final wl_data_offer.Resource    resource,
                        final String                    mimeType,
                        final int                       fd) {

    }

    @Override
    public void destroy(final wl_data_offer.Resource resource) {
        post(new ResourceDestroyed(resource));
        resource.destroy();
    }
}

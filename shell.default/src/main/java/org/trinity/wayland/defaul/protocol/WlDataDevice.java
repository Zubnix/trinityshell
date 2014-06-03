package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_data_device;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;

import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@AutoFactory(className = "WlDataDeviceFactory")
public class WlDataDevice extends EventBus implements wl_data_device.Requests, ProtocolObject<wl_data_device.Resource> {

    private final Set<wl_data_device.Resource> resources = Sets.newHashSet();

    private final WlSeat wlSeat;

    WlDataDevice(final WlSeat wlSeat) {
        this.wlSeat = wlSeat;
    }

    @Override
    public void startDrag(final wl_data_device.Resource resource,
                          final Resource                source,
                          final Resource                origin,
                          final Resource                icon,
                          final int                     serial) {

    }

    @Override
    public void setSelection(final wl_data_device.Resource  resource,
                             final Resource                 source,
                             final int                      serial) {

    }

    @Override
    public Set<wl_data_device.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_data_device.Resource create(final Client client,
                                          final int version,
                                          final int id) {
        return new wl_data_device.Resource(client,
                                           version,
                                           id);
    }
}

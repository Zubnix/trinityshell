package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlDataDeviceRequests;
import org.freedesktop.wayland.server.WlDataDeviceResource;
import org.freedesktop.wayland.server.WlDataSourceResource;
import org.freedesktop.wayland.server.WlSurfaceResource;

import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@AutoFactory(className = "WlDataDeviceFactory")
public class WlDataDevice extends EventBus implements WlDataDeviceRequests, ProtocolObject<WlDataDeviceResource> {

    private final Set<WlDataDeviceResource> resources = Sets.newHashSet();

    private final WlSeat wlSeat;

    WlDataDevice(final WlSeat wlSeat) {
        this.wlSeat = wlSeat;
    }

    @Override
    public void startDrag(final WlDataDeviceResource requester,
                          final WlDataSourceResource source,
                          final WlSurfaceResource origin,
                          final WlSurfaceResource icon,
                          final int serial) {

    }

    @Override
    public void setSelection(final WlDataDeviceResource requester,
                             final WlDataSourceResource source,
                             final int serial) {

    }

    @Override
    public Set<WlDataDeviceResource> getResources() {
        return this.resources;
    }

    @Override
    public WlDataDeviceResource create(final Client client,
                                          final int version,
                                          final int id) {
        return new WlDataDeviceResource(client,
                                        version,
                                        id,
                                        this);
    }
}

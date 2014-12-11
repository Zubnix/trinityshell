package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.*;

import java.util.Set;

@AutoFactory(className = "WlDataDeviceFactory")
public class WlDataDevice extends EventBus implements WlDataDeviceRequests, ProtocolObject<WlDataDeviceResource> {

    private final Set<WlDataDeviceResource> resources = Sets.newHashSet();

    WlDataDevice() {}

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

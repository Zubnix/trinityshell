package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import org.freedesktop.wayland.protocol.wl_data_device;
import org.freedesktop.wayland.server.Resource;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@AutoFactory
public class WlDataDevice implements wl_data_device.Requests {

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
}

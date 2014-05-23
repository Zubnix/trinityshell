package org.trinity.wayland.defaul;


import org.freedesktop.wayland.protocol.wl_seat;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.inject.Inject;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
public class WlSeat extends Global implements wl_seat.Requests {

    @Inject
    WlSeat(final Display display) {
        super(display,
              wl_seat.WAYLAND_INTERFACE,
              1);
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id) {
        new wl_seat.Resource(client,
                             1,
                             id).setImplementation(this);
    }

    @Override
    public void getPointer(final wl_seat.Resource   resource,
                           final int                id) {

    }

    @Override
    public void getKeyboard(final wl_seat.Resource  resource,
                            final int               id) {

    }

    @Override
    public void getTouch(final wl_seat.Resource resource,
                         final int              id) {

    }
}

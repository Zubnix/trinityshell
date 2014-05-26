package org.trinity.wayland.defaul.protocol;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import org.freedesktop.wayland.protocol.wl_pointer;
import org.freedesktop.wayland.protocol.wl_seat;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import java.util.Optional;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory
public class WlSeat extends Global implements wl_seat.Requests {

    private final Optional<WlPointer>   optionalWlPointer;
    private final WlDataDevice          wlDataDevice;
    private final Optional<WlKeyboard>  optionalWlKeyboard;
    private final Optional<WlTouch>     optionalWlTouch;

    WlSeat(@Provided final Display              display,
           @Provided final WlDataDeviceFactory  wlDataDeviceFactory,
           final Optional<WlPointer>            optionalWlPointer,
           final Optional<WlKeyboard>           optionalWlKeyboard,
           final Optional<WlTouch>              optionalWlTouch) {
        super(display,
              wl_seat.WAYLAND_INTERFACE,
              1);
        this.wlDataDevice = wlDataDeviceFactory.create(this);

        this.optionalWlKeyboard = optionalWlKeyboard;
        this.optionalWlTouch = optionalWlTouch;
        this.optionalWlPointer = optionalWlPointer;
    }

    public WlDataDevice getWlDataDevice() {
        return this.wlDataDevice;
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id) {
        int capabilities = 0;
        if (this.optionalWlPointer.isPresent()) {
            capabilities |= wl_seat.CAPABILITY_POINTER;
        }
        if (this.optionalWlKeyboard.isPresent()) {
            capabilities |= wl_seat.CAPABILITY_KEYBOARD;
        }
        if(this.optionalWlTouch.isPresent()){
            capabilities |= wl_seat.CAPABILITY_TOUCH;
        }

        final wl_seat.Resource seatResource = new wl_seat.Resource(client,
                                                                   1,
                                                                   id);
        seatResource.setImplementation(this);
        seatResource.capabilities(capabilities);
    }

    @Override
    public void getPointer(final wl_seat.Resource   resource,
                           final int                id) {
        this.optionalWlPointer.ifPresent(wlPointer -> {
            wl_pointer.Resource pointerResource = new wl_pointer.Resource(resource.getClient(),
                                                                          1,
                                                                          id);
            pointerResource.setImplementation(wlPointer);
        });
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

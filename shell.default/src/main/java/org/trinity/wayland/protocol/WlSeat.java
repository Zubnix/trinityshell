package org.trinity.wayland.protocol;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_seat;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlSeatFactory")
public class WlSeat extends Global implements wl_seat.Requests, ProtocolObject<wl_seat.Resource> {

    private final Set<wl_seat.Resource> resources = Sets.newHashSet();
    private final EventBus              eventBus = new EventBus();

    private final WlDataDevice         wlDataDevice;
    private final Optional<WlPointer>  optionalWlPointer;
    private final Optional<WlKeyboard> optionalWlKeyboard;
    private final Optional<WlTouch>    optionalWlTouch;
    private final int                  capabilities;


    WlSeat(@Provided final Display    display,
           final WlDataDevice         wlDataDevice,
           final Optional<WlPointer>  optionalWlPointer,
           final Optional<WlKeyboard> optionalWlKeyboard,
           final Optional<WlTouch>    optionalWlTouch) {
        super(display,
              wl_seat.WAYLAND_INTERFACE,
              1);
        this.wlDataDevice       = wlDataDevice;
        this.optionalWlKeyboard = optionalWlKeyboard;
        this.optionalWlTouch    = optionalWlTouch;
        this.optionalWlPointer  = optionalWlPointer;

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
        this.capabilities = capabilities;
    }

    public WlDataDevice getWlDataDevice() {
        return this.wlDataDevice;
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id) {
        add(client,
                1,
                id);
    }

    @Override
    public void getPointer(final wl_seat.Resource resource,
                           final int              id) {
        this.optionalWlPointer.ifPresent(wlPointer ->
                                        wlPointer.add(resource.getClient(),
                                                      1,
                                                      id));
    }

    @Override
    public void getKeyboard(final wl_seat.Resource  resource,
                            final int               id) {
        this.optionalWlKeyboard.ifPresent(wlKeyboard ->
                                          wlKeyboard.add(resource.getClient(),
                                                         1,
                                                         id));
    }

    @Override
    public void getTouch(final wl_seat.Resource resource,
                         final int              id) {
        this.optionalWlTouch.ifPresent(wlTouch ->
                                       wlTouch.add(resource.getClient(),
                                                   1,
                                                   id));
    }

    @Override
    public Set<wl_seat.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_seat.Resource create(final Client client,
                                   final int version,
                                   final int id) {
        final wl_seat.Resource resource = new wl_seat.Resource(client,
                                                               version,
                                                               id);
        resource.capabilities(this.capabilities);
        return resource;
    }

    @Override
    public void register(@Nonnull final Object listener) {
        this.eventBus.register(listener);
    }

    @Override
    public void unregister(@Nonnull final Object listener) {
        this.eventBus.unregister(listener);
    }

    @Override
    public void post(@Nonnull final Object event) {
        this.eventBus.post(event);
    }
}

package org.trinity.wayland.protocol;


import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.freedesktop.wayland.server.WlSeatRequestsV3;
import org.freedesktop.wayland.server.WlSeatResource;
import org.freedesktop.wayland.shared.WlSeatCapability;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlSeatFactory")
public class WlSeat extends Global implements WlSeatRequestsV3, ProtocolObject<WlSeatResource> {

    private final Set<WlSeatResource> resources = Sets.newHashSet();
    private final EventBus            eventBus = new EventBus();

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
              VERSION);
        this.wlDataDevice       = wlDataDevice;
        this.optionalWlKeyboard = optionalWlKeyboard;
        this.optionalWlTouch    = optionalWlTouch;
        this.optionalWlPointer  = optionalWlPointer;

        int capabilities = 0;
        if (this.optionalWlPointer.isPresent()) {

            capabilities |= WlSeatCapability.POINTER.getValue();
        }
        if (this.optionalWlKeyboard.isPresent()) {
            capabilities |= WlSeatCapability.KEYBOARD.getValue();
        }
        if(this.optionalWlTouch.isPresent()){
            capabilities |= WlSeatCapability.TOUCH.getValue();
        }
        this.capabilities = capabilities;
    }

    public WlDataDevice getWlDataDevice() {
        return this.wlDataDevice;
    }

    @Override
    public void onBindClient(final Client client,
                             final int    version,
                             final int    id) {
        //FIXME check if we support given version.
        add(client,
            version,
            id);
    }

    @Override
    public void getPointer(final WlSeatResource resource,
                           final int            id) {
        this.optionalWlPointer.ifPresent(wlPointer ->
                                         wlPointer.add(resource.getClient(),
                                                       resource.getVersion(),
                                                       id));
    }

    @Override
    public void getKeyboard(final WlSeatResource resource,
                            final int            id) {
        this.optionalWlKeyboard.ifPresent(wlKeyboard ->
                                          wlKeyboard.add(resource.getClient(),
                                                         resource.getVersion(),
                                                         id));
    }

    @Override
    public void getTouch(final WlSeatResource resource,
                         final int            id) {
        this.optionalWlTouch.ifPresent(wlTouch ->
                                       wlTouch.add(resource.getClient(),
                                                   resource.getVersion(),
                                                   id));
    }

    @Override
    public Set<WlSeatResource> getResources() {
        return this.resources;
    }

    @Override
    public WlSeatResource create(final Client client,
                                 final int version,
                                 final int id) {
        final WlSeatResource resource = new WlSeatResource(client,
                                                           version,
                                                           id,
                                                           this);
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

package org.trinity.wayland.defaul.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_output;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/3/14.
 */
@Singleton//Eager
public class WlOutput extends Global implements wl_output.Requests, ProtocolObject<wl_output.Resource> {

    private final EventBus                eventBus  = new EventBus();
    private final Set<wl_output.Resource> resources = Sets.newHashSet();

    @Inject
    WlOutput(final Display display) {
        super(display,
              wl_output.WAYLAND_INTERFACE,
              2);
    }

    @Override
    public void bindClient(final Client client,
                           final int version,
                           final int id) {
        add(client,
            version,
            id);
    }

    @Override
    public Set<wl_output.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_output.Resource create(final Client client,
                                     final int version,
                                     final int id) {
        return new wl_output.Resource(client,
                                      version,
                                      id);
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

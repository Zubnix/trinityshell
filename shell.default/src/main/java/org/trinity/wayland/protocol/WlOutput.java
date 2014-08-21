package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.freedesktop.wayland.server.WlOutputRequestsV2;
import org.freedesktop.wayland.server.WlOutputResource;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/3/14.
 */
@Singleton//Eager
public class WlOutput extends Global implements WlOutputRequestsV2, ProtocolObject<WlOutputResource> {

    private final EventBus              eventBus  = new EventBus();
    private final Set<WlOutputResource> resources = Sets.newHashSet();

    @Inject
    WlOutput(final Display display) {
        super(display,
              VERSION);
    }

    @Override
    public void onBindClient(final Client client,
                             final int version,
                             final int id) {
        add(client,
            version,
            id);
    }

    @Override
    public Set<WlOutputResource> getResources() {
        return this.resources;
    }

    @Override
    public WlOutputResource create(final Client client,
                                     final int version,
                                     final int id) {
        return new WlOutputResource(client,
                                    version,
                                    id,
                                    this);
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

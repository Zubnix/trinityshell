package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton//Eager
public class WlOutput extends Global<WlOutputResource> implements WlOutputRequestsV2, ProtocolObject<WlOutputResource> {

    private final EventBus              eventBus  = new EventBus();
    private final Set<WlOutputResource> resources = Sets.newHashSet();

    @Inject
    WlOutput(final Display display) {
        super(display,
              WlOutputResource.class,
              VERSION);
    }

    @Override
    public WlOutputResource onBindClient(final Client client,
                                         final int version,
                                         final int id) {
        return add(client,
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

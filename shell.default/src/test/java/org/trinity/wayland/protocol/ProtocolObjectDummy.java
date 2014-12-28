package org.trinity.wayland.protocol;

import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;

public class ProtocolObjectDummy implements ProtocolObject<Resource<?>> {

    private final Set<Resource<?>> resources = new HashSet<>();

    @Override
    public Set<Resource<?>> getResources() {
        return this.resources;
    }

    @Override
    public Resource<?> create(final Client client,
                              final int version,
                              final int id) {
        return mock(Resource.class);
    }

    @Override
    public void register(@Nonnull final Object listener) {

    }

    @Override
    public void unregister(@Nonnull final Object listener) {

    }

    @Override
    public void post(@Nonnull final Object event) {

    }
}

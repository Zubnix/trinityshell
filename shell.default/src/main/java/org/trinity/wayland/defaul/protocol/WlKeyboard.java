package org.trinity.wayland.defaul.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_keyboard;
import org.freedesktop.wayland.server.Client;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Singleton
public class WlKeyboard extends EventBus implements wl_keyboard.Requests3, ProtocolObject<wl_keyboard.Resource> {

    private final Set<wl_keyboard.Resource> resources = Sets.newHashSet();

    @Inject
    WlKeyboard() {
    }

    @Override
    public void release(final wl_keyboard.Resource resource) {

    }

    @Override
    public Set<wl_keyboard.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_keyboard.Resource create(final Client client,
                                       final int version,
                                       final int id) {
        return new wl_keyboard.Resource(client,
                                        version,
                                        id);
    }
}

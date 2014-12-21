package org.trinity.wayland.protocol.events;

import org.freedesktop.wayland.server.Resource;

public class ResourceEvent {
    private final Resource<?> resource;

    public ResourceEvent(final Resource<?> resource) {
        this.resource = resource;
    }

    public Resource<?> getResource() {
        return this.resource;
    }
}

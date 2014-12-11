package org.trinity.wayland.protocol.events;

import org.freedesktop.wayland.server.Resource;

public class ResourceDestroyed extends ResourceEvent {
    public ResourceDestroyed(final Resource resource) {
        super(resource);
    }
}

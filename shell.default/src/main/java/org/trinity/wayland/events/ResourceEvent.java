package org.trinity.wayland.events;

import org.freedesktop.wayland.server.Resource;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
public class ResourceEvent {
    private final Resource resource;

    public ResourceEvent(final Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return this.resource;
    }
}

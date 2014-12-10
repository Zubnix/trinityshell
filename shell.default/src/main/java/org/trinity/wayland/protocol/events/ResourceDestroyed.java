package org.trinity.wayland.protocol.events;

import org.freedesktop.wayland.server.Resource;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
public class ResourceDestroyed extends ResourceEvent{
    public ResourceDestroyed(final Resource resource) {
        super(resource);
    }
}

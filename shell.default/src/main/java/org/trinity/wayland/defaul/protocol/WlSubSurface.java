package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_subsurface;
import org.freedesktop.wayland.server.Resource;
import org.trinity.wayland.defaul.events.ResourceDestroyed;

/**
 * Created by Erik De Rijcke on 6/2/14.
 */
@AutoFactory(className = "WlSubSurfaceFactory")
public class WlSubSurface extends EventBus implements wl_subsurface.Requests {

    WlSubSurface() {
    }

    @Override
    public void destroy(final wl_subsurface.Resource resource) {
        post(new ResourceDestroyed(resource));
        resource.destroy();
    }

    @Override
    public void setPosition(final wl_subsurface.Resource resource,
                            final int                    x,
                            final int                    y) {

    }

    @Override
    public void placeAbove(final wl_subsurface.Resource resource,
                           final Resource               sibling) {

    }

    @Override
    public void placeBelow(final wl_subsurface.Resource resource,
                           final Resource               sibling) {

    }

    @Override
    public void setSync(final wl_subsurface.Resource resource) {

    }

    @Override
    public void setDesync(final wl_subsurface.Resource resource) {

    }
}

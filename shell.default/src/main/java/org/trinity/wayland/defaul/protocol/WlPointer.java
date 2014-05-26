package org.trinity.wayland.defaul.protocol;

import org.freedesktop.wayland.protocol.wl_pointer;
import org.freedesktop.wayland.server.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
@Singleton
public class WlPointer implements wl_pointer.Requests3 {

    @Inject
    WlPointer() {
    }

    @Override
    public void release(final wl_pointer.Resource resource) {

    }

    @Override
    public void setCursor(final wl_pointer.Resource resource,
                          final int                 serial,
                          final Resource            surfaceResource,
                          final int                 hotspotX,
                          final int                 hotspotY) {

    }
}

package org.trinity.wayland.defaul;

import org.freedesktop.wayland.protocol.wl_compositor;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton
public class WlCompositor extends Global implements wl_compositor.Requests {

    private final Display   display;
    private final WlShm     shm;
    private final Renderer  renderer;

    @Inject
    WlCompositor(final Display display,
                 final WlShm shm,
                 final Renderer renderer) {
        super(display,
              wl_compositor.WAYLAND_INTERFACE,
              1);
        this.display = display;
        this.shm = shm;
        this.renderer = renderer;
    }

    @Override
    public void bindClient(final Client client,
                           final int version,
                           final int id) {
        new wl_compositor.Resource(client,
                version,
                id).setImplementation(this);
    }

    @Override
    public void createSurface(final wl_compositor.Resource resource,
                              final int id) {

    }

    @Override
    public void createRegion(final wl_compositor.Resource resource,
                             final int id) {

    }
}

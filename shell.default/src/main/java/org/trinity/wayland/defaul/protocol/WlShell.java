package org.trinity.wayland.defaul.protocol;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.protocol.wl_shell;
import org.freedesktop.wayland.protocol.wl_shell_surface;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.freedesktop.wayland.server.Resource;
import org.trinity.wayland.defaul.protocol.events.ResourceDestroyed;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//Eager
public class WlShell extends Global implements wl_shell.Requests {

    private final WlShellSurfaceFactory wlShellSurfaceFactory;

    @Inject
    WlShell(final Display               display,
            final WlShellSurfaceFactory wlShellSurfaceFactory) {
        super(display,
              wl_shell.WAYLAND_INTERFACE,
              1);
        this.wlShellSurfaceFactory = wlShellSurfaceFactory;
    }

    @Override
    public void getShellSurface(final wl_shell.Resource resource,
                                final int               id,
                                final Resource          surfaceRes) {
        final WlSurface wlSurface = (WlSurface) surfaceRes.getImplementation();
        final WlShellSurface wlShellSurface = this.wlShellSurfaceFactory.create(wlSurface);
        final wl_shell_surface.Resource shellSurfaceResource = new wl_shell_surface.Resource(resource.getClient(),
                                                                                             1,
                                                                                             id);
        shellSurfaceResource.setImplementation(wlShellSurface);
        wlSurface.register(new Object() {
            @Subscribe
            public void handle(final ResourceDestroyed event) {
                wlSurface.unregister(this);
                shellSurfaceResource.destroy();
            }
        });
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id) {
       new wl_shell.Resource(client,
                             1,
                             id).setImplementation(this);
    }
}

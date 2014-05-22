package org.trinity.wayland.defaul;

import org.freedesktop.wayland.protocol.wl_shell;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.freedesktop.wayland.server.Resource;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton
public class WlShell extends Global implements wl_shell.Requests {
    @Inject
    WlShell(final Display display) {
        super(display,
              wl_shell.WAYLAND_INTERFACE, 1);
    }

    @Override
    public void getShellSurface(final wl_shell.Resource resource,
                                final int id,
                                final Resource surfaceRes) {

    }
}

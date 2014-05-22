package org.trinity.wayland.defaul;

import org.freedesktop.wayland.protocol.wl_shm;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton
public class WlShm extends Global implements wl_shm.Requests {

    @Inject
    WlShm(final Display display) {
        super(display,
              wl_shm.WAYLAND_INTERFACE,
              1);
    }

    @Override
    public void createPool(final wl_shm.Resource resource,
                           final int id,
                           final int fd,
                           final int size) {

    }
}

package org.trinity.wayland.defaul.protocol;

import org.freedesktop.wayland.protocol.wl_output;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Erik De Rijcke on 6/3/14.
 */
@Singleton//Eager
public class WlOutput extends Global implements wl_output.Requests {

    @Inject
    WlOutput(final Display display) {
        super(display,
              wl_output.WAYLAND_INTERFACE,
              2);
    }

    @Override
    public void bindClient(final Client client,
                           final int version,
                           final int id) {
        new wl_output.Resource(client,
                               version,
                               id).setImplementation(this);
    }
}

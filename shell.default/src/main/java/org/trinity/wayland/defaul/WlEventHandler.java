package org.trinity.wayland.defaul;

import org.freedesktop.wayland.server.EventLoop;

import javax.inject.Inject;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
public class WlEventHandler implements EventLoop.FileDescriptorEventHandler {

    @Inject
    WlEventHandler() {
    }

    @Override
    public int handleFileDescriptorEvent(final int fd,
                                         final int mask) {
        return 0;
    }
}

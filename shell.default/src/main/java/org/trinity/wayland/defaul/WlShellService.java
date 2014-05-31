package org.trinity.wayland.defaul;

import com.google.common.util.concurrent.AbstractIdleService;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.EventLoop;
import org.trinity.LibC;

import javax.inject.Inject;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
public class WlShellService extends AbstractIdleService {

    private final Display        display;
    private final WlEventHandler wlEventHandler;
    private final LibC libC;

    @Inject
    WlShellService(final Display display,
                   final WlEventHandler wlEventHandler,
                   final LibC libC) {
        this.display = display;
        this.wlEventHandler = wlEventHandler;
        this.libC = libC;
    }

    @Override
    protected void startUp() {
        int[] fds = new int[2];
        this.libC.pipe(fds);
        this.display.getEventLoop()
                    .addFileDescriptor(fds[0],
                                       EventLoop.EVENT_READABLE,
                                       this.wlEventHandler);
    }

    @Override
    protected void shutDown() {

    }
}

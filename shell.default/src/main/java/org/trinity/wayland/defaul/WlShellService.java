package org.trinity.wayland.defaul;

import com.google.common.util.concurrent.AbstractIdleService;
import org.freedesktop.wayland.server.Display;

import javax.inject.Inject;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
public class WlShellService extends AbstractIdleService {

    private final Display           display;
    private final WlEventHandler    wlEventHandler;

    @Inject
    WlShellService(final Display        display,
                   final WlEventHandler wlEventHandler) {
        this.display = display;
        this.wlEventHandler = wlEventHandler;
    }

    @Override
    protected void startUp() {
//        this.display.getEventLoop()
//                    .addFileDescriptor(fd,
//                                       EventLoop.EVENT_READABLE,
//                                       this.wlEventHandler);
    }

    @Override
    protected void shutDown() {

    }
}

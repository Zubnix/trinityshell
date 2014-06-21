package org.trinity.x11;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcb.xcb_flush;

public class XShellService extends AbstractExecutionThreadService {

    private final XEventLoop      xEventLoop;
    private final XEventHandlers  xEventHandlers;
    private final XClientExplorer xClientExplorer;
    private final XTime           xTime;

    @Inject
    XShellService(final XEventLoop xEventLoop,
                  final XEventHandlers xEventHandlers,
                  final XClientExplorer xClientExplorer,
                  final XTime xTime) {
        this.xEventLoop = xEventLoop;
        this.xEventHandlers = xEventHandlers;
        this.xClientExplorer = xClientExplorer;
        this.xTime = xTime;
    }

    @Override
    protected void startUp() {
        // FIXME from config
        final String displayName = System.getenv("DISPLAY");
        final int targetScreen = 0;

        this.xEventLoop.register(this.xTime);
        this.xEventLoop.register(this.xEventHandlers);
        this.xEventLoop.open(displayName,
                             targetScreen);
        this.xClientExplorer.findClientDisplaySurfaces();
    }

    @Override
    protected void run() {
        while (isRunning()) {
            xcb_flush(this.xEventLoop.waitForEvent()
                                     .getXcbConnection());
        }
    }

    @Override
    protected void shutDown() {
        this.xEventLoop.close();
        this.xEventLoop.unregister(this.xEventHandlers);
        this.xEventLoop.unregister(this.xTime);
    }
}

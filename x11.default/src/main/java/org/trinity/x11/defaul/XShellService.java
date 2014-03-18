package org.trinity.x11.defaul;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

import javax.inject.Inject;

public class XShellService extends AbstractExecutionThreadService {

    private final XEventChannel xEventChannel;
    private final XEventHandlers xEventHandlers;
    private final XClientExplorer xClientExplorer;
    private final XTime xTime;

    @Inject
    XShellService(final XEventChannel xEventChannel,
                  final XEventHandlers xEventHandlers,
                  final XClientExplorer xClientExplorer,
                  final XTime xTime) {
        this.xEventChannel = xEventChannel;
        this.xEventHandlers = xEventHandlers;
        this.xClientExplorer = xClientExplorer;
        this.xTime = xTime;
    }

    @Override
    protected void startUp() {
        // FIXME from config
        final String displayName = System.getenv("DISPLAY");
        final int targetScreen = 0;

        this.xEventChannel.register(this.xTime);
        this.xEventChannel.register(this.xEventHandlers);
        this.xEventChannel.open(displayName,
                targetScreen);
        this.xClientExplorer.findClientDisplaySurfaces();
    }

    @Override
    protected void run() {
        while (isRunning()) {
            this.xEventChannel.pump();
        }
    }

    @Override
    protected void shutDown() {
        this.xEventChannel.close();
        this.xEventChannel.unregister(this.xEventHandlers);
        this.xEventChannel.unregister(this.xTime);
    }
}

package org.trinity.wayland;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.freedesktop.wayland.server.Display;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
public class WlShellService extends AbstractExecutionThreadService {

    private final Display       display;
    private final WlJobExecutor wlJobExecutor;

    @Inject
    WlShellService(final Display display,
                   final WlJobExecutor wlJobExecutor) {
        this.display = display;
        this.wlJobExecutor = wlJobExecutor;
    }

    @Override
    protected void startUp() throws
                             IOException {
        this.wlJobExecutor.start();
    }

    @Override
    protected void run() {
        this.display.addSocket("wayland-0");
        this.display.run();
    }

    @Override
    protected void shutDown() throws IOException {
        this.display.terminate();
        this.wlJobExecutor.fireFinishedEvent();
    }
}
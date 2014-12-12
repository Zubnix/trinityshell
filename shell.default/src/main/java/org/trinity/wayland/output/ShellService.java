package org.trinity.wayland.output;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import org.freedesktop.wayland.server.Display;

import javax.inject.Inject;
import java.io.IOException;

public class ShellService extends AbstractExecutionThreadService {

    private final Display       display;
    private final JobExecutor jobExecutor;

    @Inject
    ShellService(final Display display,
                 final JobExecutor jobExecutor) {
        this.display = display;
        this.jobExecutor = jobExecutor;
    }

    @Override
    protected void startUp() throws IOException {
        this.jobExecutor.start();
    }

    @Override
    protected void run() {
        this.display.initShm();
        this.display.addSocket("wayland-0");
        this.display.run();
    }

    @Override
    protected void shutDown() throws IOException {
        this.display.terminate();
        this.jobExecutor.fireFinishedEvent();
    }
}
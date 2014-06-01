package org.trinity.wayland.defaul;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractIdleService;
import jnr.constants.platform.Fcntl;
import jnr.ffi.LastError;
import jnr.ffi.Runtime;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.LibC;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
public class WlShellService extends AbstractIdleService implements EventLoop.FileDescriptorEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WlShellService.class);

    private static final int                  FD_CLOEXEC          = 1;
    private static final byte                 EVENT_NEW_JOB       = 1;
    private final        ByteBuffer           newJobEventBuffer   = ByteBuffer.allocateDirect(1)
                                                                              .putInt(EVENT_NEW_JOB);
    private static final byte                 EVENT_FINISHED      = 0;
    private final        ByteBuffer           finishedEventBuffer = ByteBuffer.allocateDirect(1)
                                                                              .putInt(EVENT_FINISHED);
    private final        ByteBuffer           eventReadBuffer     = ByteBuffer.allocateDirect(1);
    private final        LinkedList<Runnable> jobs                = Lists.newLinkedList();

    private final Display display;
    private final LibC    libC;

    private EventLoop.EventSource eventSource;
    private int[]                 pipeFds;

    @Inject
    WlShellService(final Display display,
                   final LibC libC) {
        this.display = display;
        this.libC = libC;
    }

    @Override
    protected void startUp() throws IOException {
        this.pipeFds = configurePipe(createPipe());
        this.eventSource = this.display.getEventLoop()
                                       .addFileDescriptor(pipeFds[0],
                                                          EventLoop.EVENT_READABLE,
                                                          this);
    }

    private int[] createPipe() throws IOException {
        int[] pipeFds = new int[2];
        if (this.libC.pipe(pipeFds) == -1) {
            throw new IOException(getLastErrorString());
        }
        return pipeFds;
    }

    public String getLastErrorString() {
        return this.libC.strerror(LastError.getLastError(Runtime.getRuntime(this.libC)));
    }

    private int[] configurePipe(final int[] pipeFds) throws IOException {

        final int readFd = pipeFds[0];
        final int writeFd = pipeFds[1];

        int readFlags = this.libC.fcntl(readFd,
                                        Fcntl.F_GETFD.intValue(),
                                        0);
        if (readFlags == -1) {
            throw new IOException(getLastErrorString());
        }
        if (this.libC.fcntl(readFd,
                            Fcntl.F_SETFD.intValue(),
                            readFlags |= FD_CLOEXEC) == -1) {
            throw new IOException(getLastErrorString());
        }

        int writeFlags = this.libC.fcntl(writeFd,
                                         Fcntl.F_GETFD.intValue(),
                                         0);
        if (writeFlags == -1) {
            throw new IOException(getLastErrorString());
        }
        if (this.libC.fcntl(writeFd,
                            Fcntl.F_SETFD.intValue(),
                            writeFlags |= FD_CLOEXEC) == -1) {
            throw new IOException(getLastErrorString());
        }

        return pipeFds;
    }

    @Override
    protected void shutDown() throws IOException {
        if (this.libC.write(this.pipeFds[1],
                            this.finishedEventBuffer,
                            1) != 1) {
            throw new IOException(getLastErrorString());
        }
    }

    private void cleanUp() {
        if (this.libC.close(this.pipeFds[0]) == -1) {
            LOGGER.error("Failed to close pipe read fd",
                         new IOException(getLastErrorString()));
        }
        if (this.libC.close(this.pipeFds[1]) == -1) {
            LOGGER.error("Failed to close pipe write fd",
                         new IOException(getLastErrorString()));
        }

        this.display.getEventLoop()
                    .remove(this.eventSource);
    }

    @Override
    public int handleFileDescriptorEvent(final int fd,
                                         final int mask) {
        do {
            this.eventReadBuffer.clear();
            this.libC.read(this.pipeFds[0],
                           this.eventReadBuffer,
                           1);
            this.eventReadBuffer.rewind();
            handleEvent(this.eventReadBuffer.get());
        } while (!this.jobs.isEmpty());

        return 0;
    }

    private void handleEvent(final byte event) {
        if (event == EVENT_FINISHED) {
            cleanUp();
        }
        else if (event == EVENT_NEW_JOB) {
            this.jobs.poll()
                     .run();
        }
        else {
            throw new IllegalStateException();
        }
    }

    public void offer(Runnable job) {
        try {
            this.jobs.offer(job);
            jobNew();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void jobNew() throws IOException {
        if (this.libC.write(this.pipeFds[1],
                            newJobEventBuffer,
                            1) != 1) {
            throw new IOException(getLastErrorString());
        }
    }
}
package org.trinity.wayland.defaul;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import jnr.constants.platform.Fcntl;
import jnr.ffi.LastError;
import jnr.ffi.Runtime;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.EventLoop;
import org.trinity.LibC;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * Created by Erik De Rijcke on 5/26/14.
 */
public class WlShellService extends AbstractIdleService {

    private static final int        FD_CLOEXEC         = 1;
    private static final ByteBuffer EVENT_JOB_NEW      = ByteBuffer.allocateDirect(1)
                                                                   .putInt(1);
    private static final ByteBuffer EVENT_JOB_FINISHED = ByteBuffer.allocateDirect(1)
                                                                   .putInt(0);

    private final ListeningExecutorService executor = listeningDecorator(newSingleThreadExecutor());

    private final Display        display;
    private final WlEventHandler wlEventHandler;
    private final LibC           libC;

    private EventLoop.EventSource eventSource;
    private int[]                 pipeFds;

    @Inject
    WlShellService(final Display display,
                   final WlEventHandler wlEventHandler,
                   final LibC libC) {
        this.display = display;
        this.wlEventHandler = wlEventHandler;
        this.libC = libC;
    }

    @Override
    protected void startUp() throws IOException {
        this.pipeFds = configurePipe(createPipe());
        this.eventSource = this.display.getEventLoop()
                                       .addFileDescriptor(pipeFds[0],
                                                          EventLoop.EVENT_READABLE,
                                                          this.wlEventHandler);
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

    private int[] configurePipe(int[] pipeFds) throws IOException {

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
        if (this.libC.close(this.pipeFds[0]) == -1) {
            throw new IOException(getLastErrorString());
        }
        if (this.libC.close(this.pipeFds[1]) == -1) {
            throw new IOException(getLastErrorString());
        }

        this.display.getEventLoop()
                    .remove(this.eventSource);
    }

    public <T> ListenableFuture<T> run(Callable<T> job) {
        try {
            jobStarted();
            final ListenableFuture<T> future = this.executor.submit(job);
            addCallback(future,
                        new FutureCallback<T>() {
                            @Override
                            public void onSuccess(@Nullable T t) {
                                jobFinished();
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
            return future;
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void jobStarted() throws IOException {
        if (this.libC.write(this.pipeFds[1],
                            EVENT_JOB_NEW,
                            1) != 1) {
            throw new IOException(getLastErrorString());
        }
    }

    private void jobFinished() throws IOException {
        if (this.libC.write(this.pipeFds[1],
                            EVENT_JOB_FINISHED,
                            1) != 1) {
            throw new IOException(getLastErrorString());
        }
    }
}

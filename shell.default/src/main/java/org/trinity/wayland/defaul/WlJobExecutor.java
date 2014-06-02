package org.trinity.wayland.defaul;

import com.google.common.collect.Lists;
import jnr.ffi.LastError;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;
import static jnr.constants.platform.Fcntl.F_GETFD;
import static jnr.constants.platform.Fcntl.F_SETFD;

/**
 * Created by Erik De Rijcke on 6/2/14.
 */
@Singleton
public class WlJobExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(WlJobExecutor.class);

    private static final int        FD_CLOEXEC     = 1;
    private static final byte       EVENT_NEW_JOB  = 1;
    private static final byte       EVENT_FINISHED = 0;
    private static final Runnable[] NO_JOBS        = new Runnable[0];

    private final ByteBuffer eventNewJobBuffer   = ByteBuffer.allocateDirect(1)
                                                             .putInt(EVENT_NEW_JOB);
    private final ByteBuffer eventFinishedBuffer = ByteBuffer.allocateDirect(1)
                                                             .putInt(EVENT_FINISHED);
    private final ByteBuffer eventReadBuffer     = ByteBuffer.allocateDirect(1);

    private final ReentrantLock        jobsLock    = new ReentrantLock();
    private final LinkedList<Runnable> pendingJobs = Lists.newLinkedList();

    private final Display display;
    private final LibC    libC;

    private EventLoop.EventSource eventSource;
    private int[]                 pipe;

    @Inject
    WlJobExecutor(final Display display,
                  final LibC    libC) {
        this.display = display;
        this.libC    = libC;
    }

    public void start() throws IOException {
        //FIXME move setting pipe up to outside construction logic
        this.pipe        = configure(pipe());
        this.eventSource = this.display.getEventLoop()
                                       .addFileDescriptor(this.pipe[0],
                                                          EventLoop.EVENT_READABLE,
                                                          this::handle);
    }

    public void fireFinishedEvent() throws IOException {
        if (this.libC.write(this.pipe[1],
                            this.eventFinishedBuffer,
                            1) != 1) {
            throw new IOException(getError());
        }
    }

    public void offer(@Nonnull final Runnable job){
        checkNotNull(job);

        try {
            this.jobsLock.lock();
            this.pendingJobs.add(job);
            fireNewJobEvent();
        } catch (final IOException e) {
            //"rollback"
            this.pendingJobs.remove(job);
            LOGGER.error("Can not offer job", e);
        } finally {
            this.jobsLock.unlock();
        }
    }

    private void clean() {
        if (this.libC.close(this.pipe[0]) == -1) {
            LOGGER.error("Failed to close pipe read fd",
                    new IOException(getError()));
        }
        if (this.libC.close(this.pipe[1]) == -1) {
            LOGGER.error("Failed to close pipe write fd",
                    new IOException(getError()));
        }

        this.display.getEventLoop()
                    .remove(this.eventSource);
    }

    private int handle(final int fd,
                       final int mask){
        final byte event = read();
        if (event == EVENT_FINISHED) {
            clean();
        } else if (event == EVENT_NEW_JOB) {
            process();
        } else {
            throw new IllegalStateException("Got illegal event code " + event);
        }
        return 0;
    }

    private byte read(){
        this.eventReadBuffer.clear();
        this.libC.read(this.pipe[0],
                       this.eventReadBuffer,
                       1);
        this.eventReadBuffer.rewind();
        return this.eventReadBuffer.get();
    }

    private void process() {
        for(final Runnable job: flush()){
            job.run();
        }
    }

    private Runnable[] flush(){
        Runnable[] jobs = NO_JOBS;
        try {
            this.jobsLock.lock();
            if(!this.pendingJobs.isEmpty()){
                jobs = this.pendingJobs.toArray(new Runnable[this.pendingJobs.size()]);
                this.pendingJobs.clear();
            }
        } finally {
            this.jobsLock.unlock();
        }
        return jobs;
    }

    private void fireNewJobEvent() throws IOException {
        if (this.libC.write(this.pipe[1],
                            this.eventNewJobBuffer,
                            1) != 1) {
            throw new IOException(getError());
        }
    }

    private int[] pipe() throws IOException {
        final int[] pipeFds = new int[2];
        if (this.libC.pipe(pipeFds) == -1) {
            throw new IOException(getError());
        }
        return pipeFds;
    }

    private int[] configure(final int[] pipeFds) throws IOException {
        final int readFd  = pipeFds[0];
        final int writeFd = pipeFds[1];

        final int readFlags = this.libC.fcntl(readFd,
                                              F_GETFD.intValue(),
                                              0);
        if (readFlags == -1) {
            throw new IOException(getError());
        }
        if (this.libC.fcntl(readFd,
                            F_SETFD.intValue(),
                            readFlags | FD_CLOEXEC) == -1) {
            throw new IOException(getError());
        }

        final int writeFlags = this.libC.fcntl(writeFd,
                                               F_GETFD.intValue(),
                                               0);
        if (writeFlags == -1) {
            throw new IOException(getError());
        }
        if (this.libC.fcntl(writeFd,
                            F_SETFD.intValue(),
                            writeFlags | FD_CLOEXEC) == -1) {
            throw new IOException(getError());
        }

        return pipeFds;
    }

    private String getError() {
        return this.libC.strerror(LastError.getLastError(jnr.ffi.Runtime.getRuntime(this.libC)));
    }
}

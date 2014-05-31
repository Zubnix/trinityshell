package org.trinity;

import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.types.size_t;
import jnr.ffi.types.ssize_t;

import java.nio.ByteBuffer;

public interface LibC {

    public int close(int fd);

    @ssize_t
    public int read(int fd,
                    @Out ByteBuffer data,
                    @size_t long size);

    @ssize_t
    public int write(int fd,
                     @In ByteBuffer data,
                     @size_t long size);

    public int pipe(@Out int[] fds);
}
package org.trinity.wayland;

import jnr.ffi.annotations.IgnoreError;
import jnr.ffi.annotations.In;
import jnr.ffi.annotations.Out;
import jnr.ffi.types.size_t;
import jnr.ffi.types.ssize_t;

import java.nio.ByteBuffer;

public interface LibC {

    int close(int fd);

    int fcntl(int fd,
              int cmd,
              int data);

    @IgnoreError
    String strerror(int error);

    @ssize_t
    int read(int fd,
             @Out ByteBuffer data,
             @size_t long size);

    @ssize_t
    int write(int fd,
              @In ByteBuffer data,
              @size_t long size);

    int pipe(@Out int[] fds);
}
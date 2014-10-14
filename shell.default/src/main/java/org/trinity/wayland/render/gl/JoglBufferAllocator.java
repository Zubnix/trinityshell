package org.trinity.wayland.render.gl;

import com.hackoeur.jglm.buffer.BufferAllocator;
import com.jogamp.common.nio.Buffers;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class JoglBufferAllocator implements BufferAllocator {
    @Override
    public ByteBuffer allocateByteBuffer(final int sizeInBytes) {
        return Buffers.newDirectByteBuffer(sizeInBytes);
    }

    @Override
    public FloatBuffer allocateFloatBuffer(final int sizeInFloats) {
        return Buffers.newDirectFloatBuffer(sizeInFloats);
    }
}

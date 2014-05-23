package org.trinity.wayland.defaul;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Throwables;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.ShmPool;
import org.freedesktop.wayland.protocol.wl_buffer;
import org.freedesktop.wayland.protocol.wl_shm_pool;
import org.trinity.wayland.defaul.events.ResourceDestroyed;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.media.nativewindow.util.Dimension;

import java.io.IOException;
import java.nio.ByteBuffer;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory
public class WlShmPool implements wl_shm_pool.Requests {

    private final ShmPool               shmPool;
    private final WlShmBufferFactory    wlShmBufferFactory;

    private boolean destroyed   = false;
    private int     refCount    =  0;

    WlShmPool(@Provided final WlShmBufferFactory    wlShmBufferFactory,
              @Nonnull  final ShmPool               shmPool) {
        this.wlShmBufferFactory = wlShmBufferFactory;
        this.shmPool = shmPool;
    }

    @Override
    public void createBuffer(final wl_shm_pool.Resource resource,
                             final int                  id,
                             @Nonnegative final int     offset,
                             @Nonnegative final int     width,
                             @Nonnegative final int     height,
                             @Nonnegative final int     stride,
                             final int format) {
        checkArgument(width > 0);
        checkArgument(height > 0);
        checkArgument(stride > 0);
        checkArgument(offset > 0);

        this.refCount++;
        final ByteBuffer byteBuffer = this.shmPool.asByteBuffer();
        byteBuffer.position(offset);
        final WlShmBuffer wlShmBuffer = this.wlShmBufferFactory.create(byteBuffer.slice(),
                                                                       new Dimension(width,
                                                                                     height),
                                                                       stride,
                                                                       format);
        wlShmBuffer.register(new Object() {
            @Subscribe
            public void handle(final ResourceDestroyed resourceEvent) {
                wlShmBuffer.unregister(this);
                WlShmPool.this.refCount--;
                release();
            }
        });
        new wl_buffer.Resource(resource.getClient(),
                               1,
                               id).setImplementation(wlShmBuffer);
    }

    @Override
    public void destroy(final wl_shm_pool.Resource resource) {
        this.destroyed = true;
        release();
        resource.destroy();
    }

    @Override
    public void resize(final wl_shm_pool.Resource   resource,
                       final int                    size) {
        try {
            this.shmPool.resize(size);
        } catch (final IOException e) {
            Throwables.propagate(e);
        }
    }

    private void release() {
        if (this.refCount == 0 && this.destroyed){
            try {
                this.shmPool.close();
            } catch (final java.io.IOException e) {
                Throwables.propagate(e);
            }
        }
    }
}

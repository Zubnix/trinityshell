package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.ShmPool;
import org.freedesktop.wayland.protocol.wl_shm_pool;
import org.freedesktop.wayland.server.Client;
import org.trinity.wayland.defaul.events.ResourceDestroyed;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.media.nativewindow.util.Dimension;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlShmPoolFactory")
public class WlShmPool extends EventBus implements wl_shm_pool.Requests, ProtocolObject<wl_shm_pool.Resource> {

    private final Set<wl_shm_pool.Resource> resources = Sets.newHashSet();

    private final ShmPool            shmPool;
    private final WlShmBufferFactory wlShmBufferFactory;

    private boolean destroyed = false;
    private int refCount      = 0;

    WlShmPool(@Provided final WlShmBufferFactory wlShmBufferFactory,
              @Nonnull  final ShmPool            shmPool) {
        this.wlShmBufferFactory = wlShmBufferFactory;
        this.shmPool            = shmPool;
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
        wlShmBuffer.add(resource.getClient(),
                        1,
                        id);
        wlShmBuffer.register(new Object() {
            @Subscribe
            public void handle(final ResourceDestroyed resourceEvent) {
                wlShmBuffer.unregister(this);
                WlShmPool.this.refCount--;
                release();
            }
        });

    }

    @Override
    public Set<wl_shm_pool.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_shm_pool.Resource create(final Client client,
                                       final int version,
                                       final int id) {
        return new wl_shm_pool.Resource(client,
                                        version,
                                        id);
    }

    @Override
    public void destroy(final wl_shm_pool.Resource resource) {
        this.destroyed = true;
        release();
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void resize(final wl_shm_pool.Resource resource,
                       final int                  size) {
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

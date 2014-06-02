package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.protocol.wl_buffer;
import org.trinity.shell.scene.api.Buffer;
import org.trinity.wayland.defaul.WlShmBufferRenderer;
import org.trinity.wayland.defaul.events.ResourceDestroyed;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.media.nativewindow.util.DimensionImmutable;
import java.nio.ByteBuffer;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlShmBufferFactory")
public class WlShmBuffer extends EventBus implements wl_buffer.Requests, Buffer{

    private final ByteBuffer         byteBuffer;
    private final DimensionImmutable size;
    private final int                stride;
    private final int                format;

    private final EventBus visitorDispatcher = new EventBus(){{
        register(new Object() {
            @Subscribe
            public void handle(@Nonnull final WlShmBufferRenderer wlShmBufferRenderer) {
                wlShmBufferRenderer.visit(WlShmBuffer.this);
            }
        });
    }};

    WlShmBuffer(@Nonnull final ByteBuffer         byteBuffer,
                @Nonnull final DimensionImmutable size,
                @Nonnegative final int            stride,
                final int format) {
        this.byteBuffer = byteBuffer;
        this.size = size;
        this.stride = stride;
        this.format = format;
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    public int getStride() {
        return this.stride;
    }

    public int getFormat() {
        return this.format;
    }

    @Override
    public void destroy(final wl_buffer.Resource resource) {
        resource.destroy();
        post(new ResourceDestroyed(resource));
    }

    @Override
    public void accept(@Nonnull final Object renderer) {
        //We (ab)use guava's eventbus as a dynamic type based dispatcher. That way we don't have to cast!
        //Any unsupported renderer will simply be ignored. To detect any unsupported render, simply listen
        //for guava's deadevent object.
        this.visitorDispatcher.post(renderer);
    }

    @Nonnull
    @Override
    public DimensionImmutable getSize() {
        return this.size;
    }
}

package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_buffer;
import org.freedesktop.wayland.server.Client;
import org.trinity.shell.scene.api.Buffer;
import org.trinity.wayland.defaul.WlShmRenderer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.media.nativewindow.util.DimensionImmutable;
import java.nio.ByteBuffer;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlShmBufferFactory")
public class WlShmBuffer extends EventBus implements wl_buffer.Requests, Buffer, ProtocolObject<wl_buffer.Resource>{

    private final Set<wl_buffer.Resource> resources  = Sets.newHashSet();
    private final EventBus                dispatcher = WlShmRenderer.DISPATCHER(this);

    private final ByteBuffer         byteBuffer;
    private final DimensionImmutable size;
    private final int                stride;
    private final int                format;


    WlShmBuffer(@Nonnull     final ByteBuffer         byteBuffer,
                @Nonnull     final DimensionImmutable size,
                @Nonnegative final int                stride,
                             final int                format) {
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
    public Set<wl_buffer.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_buffer.Resource create(final Client client,
                                     final int version,
                                     final int id) {
        return new wl_buffer.Resource(client,
                                      version,
                                      id);
    }

    @Override
    public void destroy(final wl_buffer.Resource resource) {
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void accept(@Nonnull final Object renderer) {
        //We (ab)use guava's eventbus as a dynamic type based dispatcher. That way we don't have to cast!
        //Any unsupported renderer will simply be ignored. To detect any unsupported render, simply listen
        //for guava's deadevent object.
        this.dispatcher.post(renderer);
    }

    @Nonnull
    @Override
    public DimensionImmutable getSize() {
        return this.size;
    }
}

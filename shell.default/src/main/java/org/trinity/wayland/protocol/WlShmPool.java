package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.ShmBuffer;
import org.freedesktop.wayland.server.WlShmPoolRequests;
import org.freedesktop.wayland.server.WlShmPoolResource;

import javax.annotation.Nonnegative;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlShmPoolFactory")
public class WlShmPool extends EventBus implements WlShmPoolRequests, ProtocolObject<WlShmPoolResource> {

    private final Set<WlShmPoolResource> resources = Sets.newHashSet();

    WlShmPool() {
    }

    @Override
    public void createBuffer(final WlShmPoolResource resource,
                             final int               id,
                             @Nonnegative
                             final int               offset,
                             @Nonnegative
                             final int               width,
                             @Nonnegative
                             final int               height,
                             @Nonnegative
                             final int               stride,
                             final int               format) {
        checkArgument(width > 0,
                      "Width was %s but expected nonnegative, nonzero",
                      width);
        checkArgument(height > 0,
                      "Height was %s but expected nonnegative, nonzero",
                      height);
        checkArgument(stride > 0,
                      "Stride was %s but expected nonnegative, nonzero",
                      stride);
        checkArgument(offset >= 0,
                      "Offset was %s but expected nonnegative",
                      offset);


        new ShmBuffer(resource.getClient(),
                      id,
                      width,
                      height,
                      stride,
                      format);
    }

    @Override
    public Set<WlShmPoolResource> getResources() {
        return this.resources;
    }

    @Override
    public WlShmPoolResource create(final Client client,
                                       final int version,
                                       final int id) {
        return new WlShmPoolResource(client,
                                     version,
                                     id,
                                     this);
    }

    @Override
    public void destroy(final WlShmPoolResource resource) {
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void resize(final WlShmPoolResource resource,
                       final int               size) {
    }
}

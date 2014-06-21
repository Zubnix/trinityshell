package org.trinity.wayland.protocol;

import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.ShmPool;
import org.freedesktop.wayland.protocol.wl_shm;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//Eager
public class WlShm extends Global implements wl_shm.Requests, ProtocolObject<wl_shm.Resource> {

    private final Set<wl_shm.Resource> resources = Sets.newHashSet();
    private final EventBus             eventBus  = new EventBus();

    private final WlShmPoolFactory wlShmPoolFactory;

    @Inject
    WlShm(final Display             display,
          final WlShmPoolFactory    wlShmPoolFactory) {
        super(display,
              wl_shm.WAYLAND_INTERFACE,
              1);
        this.wlShmPoolFactory = wlShmPoolFactory;
    }

    @Override
    public void createPool(final wl_shm.Resource resource,
                           final int             id,
                           final int             fd,
                           final int             size) {
        try {
            this.wlShmPoolFactory.create(ShmPool.fromFileDescriptor(fd,
                                                                    size,
                                                                    false,
                                                                    true)).add(resource.getClient(),
                                                                               1,
                                                                               id);
        } catch (final IOException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id){
        publish(add(client,
                version,
                id));
    }

    private void publish(final wl_shm.Resource res){
        res.format(wl_shm.FORMAT_ARGB8888);
        res.format(wl_shm.FORMAT_XRGB8888);
    }

    @Override
    public Set<wl_shm.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_shm.Resource create(final Client client,
                                  final int version,
                                  final int id) {
        return new wl_shm.Resource(client,
                                   version,
                                   id);
    }

    @Override
    public void register(@Nonnull final Object listener) {
        this.eventBus.register(listener);
    }

    @Override
    public void unregister(@Nonnull final Object listener) {
        this.eventBus.unregister(listener);
    }

    @Override
    public void post(@Nonnull final Object event) {
        this.eventBus.post(event);
    }
}

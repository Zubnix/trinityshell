package org.trinity.wayland.defaul;

import com.google.common.base.Throwables;
import org.freedesktop.wayland.ShmPool;
import org.freedesktop.wayland.protocol.wl_shm;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//Eager
public class WlShm extends Global implements wl_shm.Requests {

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
    public void createPool(final wl_shm.Resource    resource,
                           final int                id,
                           final int                fd,
                           final int                size) {
        try {
            this.wlShmPoolFactory.create(ShmPool.fromFileDescriptor(fd,
                                                                    size,
                                                                    false,
                                                                    true));
        } catch (final IOException e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id){
        final wl_shm.Resource res = new wl_shm.Resource(client,
                                                        1,
                                                        id);
        res.setImplementation(this);
        publishFormats(res);
    }

    private void publishFormats(final wl_shm.Resource res){
        res.format(wl_shm.FORMAT_ARGB8888);
        res.format(wl_shm.FORMAT_XRGB8888);
    }
}

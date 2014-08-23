package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.freedesktop.wayland.server.WlShmRequests;
import org.freedesktop.wayland.server.WlShmResource;
import org.freedesktop.wayland.shared.WlShmFormat;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//Eager
public class WlShm extends Global<WlShmResource> implements WlShmRequests, ProtocolObject<WlShmResource> {

    private final Set<WlShmResource> resources = Sets.newHashSet();
    private final EventBus           eventBus  = new EventBus();

    private final WlShmPoolFactory wlShmPoolFactory;

    @Inject
    WlShm(final Display             display,
          final WlShmPoolFactory    wlShmPoolFactory) {
        super(display,
              WlShmResource.class,
              VERSION);
        this.wlShmPoolFactory = wlShmPoolFactory;
    }

    @Override
    public void createPool(final WlShmResource resource,
                           final int           id,
                           final int           fd,
                           final int           size) {
            this.wlShmPoolFactory.create().add(resource.getClient(),
                                               VERSION,
                                               id);
    }

    @Override
    public WlShmResource onBindClient(final Client client,
                                      final int    version,
                                      final int    id){
        //FIXME check if we support requested version.
        return publish(add(client,
                           version,
                           id));
    }

    private WlShmResource publish(final WlShmResource res){
        res.format(WlShmFormat.ARGB8888.getValue());
        res.format(WlShmFormat.XRGB8888.getValue());
        return res;
    }

    @Override
    public Set<WlShmResource> getResources() {
        return this.resources;
    }

    @Override
    public WlShmResource create(final Client client,
                                  final int version,
                                  final int id) {
        return new WlShmResource(client,
                                 version,
                                 id,
                                 this);
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

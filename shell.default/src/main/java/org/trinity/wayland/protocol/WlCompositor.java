package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_compositor;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.trinity.PixmanRegionFactory;
import org.trinity.SimpleShellSurface;
import org.trinity.SimpleShellSurfaceFactory;
import org.trinity.shell.scene.api.Buffer;
import org.trinity.wayland.WlShellCompositor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//Eager
public class WlCompositor extends Global implements wl_compositor.Requests, ProtocolObject<wl_compositor.Resource> {

    private final Set<wl_compositor.Resource> resources = Sets.newHashSet();
    private final EventBus                    eventBus  = new EventBus();

    private final WlSubCompositor           subCompositor;
    private final WlSurfaceFactory          wlSurfaceFactory;
    private final WlRegionFactory           wlRegionFactory;
    private final SimpleShellSurfaceFactory simpleShellSurfaceFactory;
    private final PixmanRegionFactory       pixmanRegionFactory;
    private final WlShellCompositor         wlShellCompositor;

    @Inject
    WlCompositor(final Display                   display,
                 final WlSubCompositor           subCompositor,
                 final WlSurfaceFactory          wlSurfaceFactory,
                 final WlRegionFactory           wlRegionFactory,
                 final SimpleShellSurfaceFactory simpleShellSurfaceFactory,
                 final PixmanRegionFactory       pixmanRegionFactory,
                 final WlShellCompositor         wlShellCompositor) {
        super(display,
              wl_compositor.WAYLAND_INTERFACE,
              1);
        this.subCompositor             = subCompositor;
        this.wlSurfaceFactory          = wlSurfaceFactory;
        this.wlRegionFactory           = wlRegionFactory;
        this.simpleShellSurfaceFactory = simpleShellSurfaceFactory;
        this.pixmanRegionFactory       = pixmanRegionFactory;
        this.wlShellCompositor         = wlShellCompositor;
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id) {
        add(client,
            version,
            id);
        this.subCompositor.add(client,
                               version,
                               id);
    }

    @Override
    public void createSurface(final wl_compositor.Resource resource,
                              final int                    id) {
        final SimpleShellSurface shellSurface = this.simpleShellSurfaceFactory.create(Optional.<Buffer>empty());
        shellSurface.register(this.wlShellCompositor);
        this.wlSurfaceFactory.create(shellSurface).add(resource.getClient(),
                                                       1,
                                                       id);
    }

    @Override
    public void createRegion(final wl_compositor.Resource resource,
                             final int                    id) {
        this.wlRegionFactory.create(this.pixmanRegionFactory.create()).add(resource.getClient(),
                                                                           1,
                                                                           id);
    }

    @Override
    public Set<wl_compositor.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_compositor.Resource create(final Client client,
                                         final int version,
                                         final int id) {
        return new wl_compositor.Resource(client,
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

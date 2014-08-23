package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.freedesktop.wayland.server.WlCompositorRequestsV3;
import org.freedesktop.wayland.server.WlCompositorResource;
import org.trinity.PixmanRegionFactory;
import org.trinity.SimpleShellSurface;
import org.trinity.SimpleShellSurfaceFactory;
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
public class WlCompositor extends Global<WlCompositorResource> implements WlCompositorRequestsV3, ProtocolObject<WlCompositorResource> {

    private final Set<WlCompositorResource> resources = Sets.newHashSet();
    private final EventBus                  eventBus  = new EventBus();

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
              WlCompositorResource.class,
              VERSION);
        this.subCompositor             = subCompositor;
        this.wlSurfaceFactory          = wlSurfaceFactory;
        this.wlRegionFactory           = wlRegionFactory;
        this.simpleShellSurfaceFactory = simpleShellSurfaceFactory;
        this.pixmanRegionFactory       = pixmanRegionFactory;
        this.wlShellCompositor         = wlShellCompositor;
    }

    @Override
    public WlCompositorResource onBindClient(final Client client,
                                             final int    version,
                                             final int    id) {
        WlCompositorResource wlCompositorResource = add(client,
                                                        version,
                                                        id);
        this.subCompositor.add(client,
                               version,
                               id);
        return wlCompositorResource;
    }

    @Override
    public void createSurface(final WlCompositorResource resource,
                              final int                    id) {
        final SimpleShellSurface shellSurface = this.simpleShellSurfaceFactory.create(Optional.empty());
        shellSurface.register(this.wlShellCompositor);
        this.wlSurfaceFactory.create(shellSurface).add(resource.getClient(),
                                                       resource.getVersion(),
                                                       id);
    }

    @Override
    public void createRegion(final WlCompositorResource resource,
                             final int                  id) {
        this.wlRegionFactory.create(this.pixmanRegionFactory.create()).add(resource.getClient(),
                                                                           resource.getVersion(),
                                                                           id);
    }

    @Override
    public Set<WlCompositorResource> getResources() {
        return this.resources;
    }

    @Override
    public WlCompositorResource create(final Client client,
                                       final int version,
                                       final int id) {
        return new WlCompositorResource(client,
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

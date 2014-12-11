package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.*;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.wayland.WlShellCompositor;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;


@AutoFactory
public class WlCompositor extends Global<WlCompositorResource> implements WlCompositorRequestsV3, ProtocolObject<WlCompositorResource> {

    private final Set<WlCompositorResource> resources = Sets.newHashSet();
    private final EventBus                  eventBus  = new EventBus();

    private final WlSurfaceFactory                wlSurfaceFactory;
    private final WlRegionFactory                 wlRegionFactory;
    private final org.trinity.PixmanRegionFactory pixmanRegionFactory;
    private final WlShellCompositor               wlShellCompositor;

    @Inject
    WlCompositor(@Provided final Display display,
                 @Provided final WlSurfaceFactory wlSurfaceFactory,
                 @Provided final WlRegionFactory wlRegionFactory,
                 @Provided final org.trinity.PixmanRegionFactory pixmanRegionFactory,
                 final WlShellCompositor wlShellCompositor) {
        super(display,
              WlCompositorResource.class,
              VERSION);
        this.wlSurfaceFactory = wlSurfaceFactory;
        this.wlRegionFactory = wlRegionFactory;
        this.pixmanRegionFactory = pixmanRegionFactory;
        this.wlShellCompositor = wlShellCompositor;
    }

    @Override
    public WlCompositorResource onBindClient(final Client client,
                                             final int version,
                                             final int id) {
        return add(client,
                   version,
                   id);
    }

    @Override
    public void createSurface(final WlCompositorResource resource,
                              final int id) {
        final ShellSurface shellSurface = this.wlShellCompositor.create();
        final WlSurfaceResource wlSurfaceResource = this.wlSurfaceFactory.create(shellSurface)
                                                                         .add(resource.getClient(),
                                                                              resource.getVersion(),
                                                                              id);
        wlSurfaceResource.addDestroyListener(new Listener() {
            @Override
            public void handle() {
                shellSurface.accept(ShellSurfaceConfigurable::markDestroyed);
                destroy();
            }
        });
    }

    @Override
    public void createRegion(final WlCompositorResource resource,
                             final int id) {
        this.wlRegionFactory.create(this.pixmanRegionFactory.create())
                            .add(resource.getClient(),
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
    public void register(
            @Nonnull
            final Object listener) {
        this.eventBus.register(listener);
    }

    @Override
    public void unregister(
            @Nonnull
            final Object listener) {
        this.eventBus.unregister(listener);
    }

    @Override
    public void post(
            @Nonnull
            final Object event) {
        this.eventBus.post(event);
    }
}

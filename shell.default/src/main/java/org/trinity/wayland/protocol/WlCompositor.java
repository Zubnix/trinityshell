package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.*;
import org.trinity.wayland.output.Surface;
import org.trinity.wayland.output.SurfaceConfigurable;
import org.trinity.wayland.output.Compositor;

import javax.inject.Inject;
import java.util.Set;


@AutoFactory(className = "WlCompositorFactory")
public class WlCompositor extends Global<WlCompositorResource> implements WlCompositorRequestsV3, ProtocolObject<WlCompositorResource> {

    private final Set<WlCompositorResource> resources = Sets.newHashSet();

    private final WlSurfaceFactory                wlSurfaceFactory;
    private final WlRegionFactory                 wlRegionFactory;
    private final org.trinity.wayland.output.RegionFactory pixmanRegionFactory;
    private final Compositor                      compositor;

    @Inject
    WlCompositor(@Provided final Display display,
                 @Provided final WlSurfaceFactory wlSurfaceFactory,
                 @Provided final WlRegionFactory wlRegionFactory,
                 @Provided final org.trinity.wayland.output.RegionFactory pixmanRegionFactory,
                 final Compositor compositor) {
        super(display,
              WlCompositorResource.class,
              VERSION);
        this.wlSurfaceFactory = wlSurfaceFactory;
        this.wlRegionFactory = wlRegionFactory;
        this.pixmanRegionFactory = pixmanRegionFactory;
        this.compositor = compositor;
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
    public void createSurface(final WlCompositorResource compositorResource,
                              final int id) {
        final Surface surface = this.compositor.create();
        final WlSurface wlSurface = this.wlSurfaceFactory.create(compositorResource,
                                                                 surface);

        final WlSurfaceResource surfaceResource = wlSurface.add(compositorResource.getClient(),
                                                                compositorResource.getVersion(),
                                                                id);
        surfaceResource.addDestroyListener(new Listener() {
            @Override
            public void handle() {
                remove();
                WlCompositor.this.compositor.getScene()
                                            .getSurfacesStack()
                                            .remove(surfaceResource);
                surface.accept(SurfaceConfigurable::markDestroyed);
                WlCompositor.this.compositor.requestRender(surfaceResource);
            }
        });

        this.compositor.getScene()
                       .getSurfacesStack()
                       .push(surfaceResource);
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

    public Compositor getCompositor() {
        return this.compositor;
    }
}

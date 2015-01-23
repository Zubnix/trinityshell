package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import org.freedesktop.wayland.server.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class WlSubCompositor extends Global<WlSubcompositorResource> implements WlSubcompositorRequests, ProtocolObject<WlSubcompositorResource> {

    private final Set<WlSubcompositorResource> resources = Sets.newHashSet();

    private final WlSubSurfaceFactory wlSubSurfaceFactory;

    @Inject
    WlSubCompositor(final Display display,
                    final WlSubSurfaceFactory wlSubSurfaceFactory) {
        super(display,
              WlSubcompositorResource.class,
              VERSION);
        this.wlSubSurfaceFactory = wlSubSurfaceFactory;
    }

    @Override
    public void destroy(final WlSubcompositorResource resource) {
        resource.destroy();
    }

    @Override
    public void getSubsurface(final WlSubcompositorResource requester,
                              final int id,
                              @Nonnull final WlSurfaceResource surface,
                              @Nonnull final WlSurfaceResource parent) {
        this.wlSubSurfaceFactory.create(surface,
                                        parent)
                                .add(requester.getClient(),
                                     requester.getVersion(),
                                     id);
    }

    @Override
    public Set<WlSubcompositorResource> getResources() {
        return this.resources;
    }

    @Override
    public WlSubcompositorResource create(final Client client,
                                          final int version,
                                          final int id) {
        return new WlSubcompositorResource(client,
                                           version,
                                           id,
                                           this);
    }

    @Override
    public WlSubcompositorResource onBindClient(final Client client,
                                                final int version,
                                                final int id) {
        return new WlSubcompositorResource(client,
                                           version,
                                           id,
                                           this);
    }
}

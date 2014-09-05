package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 6/2/14.
 */
@Singleton
public class WlSubCompositor extends Global<WlSubcompositorResource> implements WlSubcompositorRequests, ProtocolObject<WlSubcompositorResource> {

    private final Set<WlSubcompositorResource> resources = Sets.newHashSet();
    private final EventBus eventBus = new EventBus();
    private final WlSubSurfaceFactory wlSubSurfaceFactory;

    @Inject
    WlSubCompositor(final Display             display,
                    final WlSubSurfaceFactory wlSubSurfaceFactory) {
        super(display,
              WlSubcompositorResource.class,
              VERSION);
        this.wlSubSurfaceFactory = wlSubSurfaceFactory;
    }

    @Override
    public void destroy(final WlSubcompositorResource resource) {
        ProtocolObject.super.destroy(resource);
    }

    @Override
    public void getSubsurface(final WlSubcompositorResource requester,
                              final int id,
                              final WlSurfaceResource surface,
                              final WlSurfaceResource parent) {
        this.wlSubSurfaceFactory.create(surface,
                                        parent).add(requester.getClient(),
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
    public void register(@Nonnull Object listener) {
        this.eventBus.register(listener);
    }

    @Override
    public void unregister(@Nonnull Object listener) {
        this.eventBus.unregister(listener);
    }

    @Override
    public void post(@Nonnull Object event) {
        this.eventBus.post(event);
    }

    @Override
    public WlSubcompositorResource onBindClient(Client client, int version, int id) {
        return new WlSubcompositorResource(client,
                                           version,
                                           id,
                                           this);
    }
}

package org.trinity.wayland.protocol;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.protocol.wl_shell;
import org.freedesktop.wayland.protocol.wl_shell_surface;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.Global;
import org.freedesktop.wayland.server.Resource;
import org.trinity.wayland.events.ResourceDestroyed;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Created by Erik De Rijcke on 5/22/14.
 */
@Singleton//Eager
public class WlShell extends Global implements wl_shell.Requests, ProtocolObject<wl_shell.Resource> {

    private final Set<wl_shell.Resource> resources = Sets.newHashSet();
    private final EventBus               eventBus  = new EventBus();

    private final WlShellSurfaceFactory wlShellSurfaceFactory;

    @Inject
    WlShell(final Display               display,
            final WlShellSurfaceFactory wlShellSurfaceFactory) {
        super(display,
              wl_shell.WAYLAND_INTERFACE,
              1);
        this.wlShellSurfaceFactory = wlShellSurfaceFactory;
    }

    @Override
    public void getShellSurface(final wl_shell.Resource resource,
                                final int               id,
                                final Resource          surfaceRes) {
        final WlSurface wlSurface = (WlSurface) surfaceRes.getImplementation();
        final WlShellSurface wlShellSurface = this.wlShellSurfaceFactory.create(wlSurface);
        final wl_shell_surface.Resource shellSurfaceResource = wlShellSurface.add(resource.getClient(),
                                                                                  1,
                                                                                  id);
        wlSurface.register(new Object() {
            @Subscribe
            public void handle(final ResourceDestroyed event) {
                wlSurface.unregister(this);
                wlShellSurface.destroy(shellSurfaceResource);
            }
        });
    }

    @Override
    public void bindClient(final Client client,
                           final int    version,
                           final int    id) {
        add(client,
                1,
                id);
    }

    @Override
    public Set<wl_shell.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_shell.Resource create(final Client client,
                                    final int version,
                                    final int id) {
        return new wl_shell.Resource(client,
                                     1,
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

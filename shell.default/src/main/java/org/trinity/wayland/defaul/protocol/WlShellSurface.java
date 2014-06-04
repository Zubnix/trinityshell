package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_shell_surface;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;

import java.util.Set;

@AutoFactory(className = "WlShellSurfaceFactory")
public class WlShellSurface extends EventBus implements wl_shell_surface.Requests, ProtocolObject<wl_shell_surface.Resource> {

    private final Set<wl_shell_surface.Resource> resources = Sets.newHashSet();

    private final WlSurface wlSurface;

    WlShellSurface(final WlSurface wlSurface) {
        this.wlSurface = wlSurface;
    }

    @Override
    public void pong(final wl_shell_surface.Resource resource,
                     final int                       serial) {

    }

    @Override
    public void move(final wl_shell_surface.Resource resource,
                     final                           Resource seatResource,
                     final                           int serial) {

    }

    @Override
    public void resize(final wl_shell_surface.Resource resource,
                       final Resource                  seatResource,
                       final int                       serial,
                       final int                       edges) {

    }

    @Override
    public void setToplevel(final wl_shell_surface.Resource resource) {

    }

    @Override
    public void setTransient(final wl_shell_surface.Resource resource,
                             final Resource                  parentResource,
                             final int                       x,
                             final int                       y,
                             final int                       flags) {

    }

    @Override
    public void setFullscreen(final wl_shell_surface.Resource resource,
                              final int                       method,
                              final int                       framerate,
                              final Resource                  output) {

    }

    @Override
    public void setPopup(final wl_shell_surface.Resource resource,
                         final Resource                  seat,
                         final int                       serial,
                         final Resource                  parent,
                         final int                       x,
                         final int                       y,
                         final int                       flags) {

    }

    @Override
    public void setMaximized(final wl_shell_surface.Resource resource,
                             final Resource                  output) {

    }

    @Override
    public void setTitle(final wl_shell_surface.Resource resource,
                         final String                    title) {

    }

    @Override
    public void setClass(final wl_shell_surface.Resource resource,
                         final String                    class_) {

    }

    @Override
    public Set<wl_shell_surface.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_shell_surface.Resource create(final Client client,
                                            final int version,
                                            final int id) {
        return new wl_shell_surface.Resource(client,
                                             version,
                                             id);
    }
}

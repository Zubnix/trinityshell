package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import org.freedesktop.wayland.protocol.wl_shell_surface;
import org.freedesktop.wayland.server.Resource;

@AutoFactory
public class WlShellSurface implements wl_shell_surface.Requests {

    private final WlSurface wlSurface;

    WlShellSurface(final WlSurface wlSurface) {
        this.wlSurface = wlSurface;
    }

    @Override
    public void pong(final wl_shell_surface.Resource    resource,
                     final int                          serial) {

    }

    @Override
    public void move(final wl_shell_surface.Resource    resource,
                     final                              Resource seatResource,
                     final                              int serial) {

    }

    @Override
    public void resize(final wl_shell_surface.Resource  resource,
                       final Resource                   seatResource,
                       final int                        serial,
                       final int                        edges) {

    }

    @Override
    public void setToplevel(final wl_shell_surface.Resource resource) {

    }

    @Override
    public void setTransient(final wl_shell_surface.Resource    resource,
                             final Resource                     parentResource,
                             final int                          x,
                             final int                          y,
                             final int                          flags) {

    }

    @Override
    public void setFullscreen(final wl_shell_surface.Resource   resource,
                              final int                         method,
                              final int                         framerate,
                              final                             Resource output) {

    }

    @Override
    public void setPopup(final wl_shell_surface.Resource    resource,
                         final Resource                     seat,
                         final int                          serial,
                         final Resource                     parent,
                         final int                          x,
                         final int                          y,
                         final int                          flags) {

    }

    @Override
    public void setMaximized(final wl_shell_surface.Resource    resource,
                             final Resource                     output) {

    }

    @Override
    public void setTitle(final wl_shell_surface.Resource    resource,
                         final String                       title) {

    }

    @Override
    public void setClass(final wl_shell_surface.Resource resource,
                         final String class_) {

    }
}

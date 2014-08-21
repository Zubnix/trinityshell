package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.WlOutputResource;
import org.freedesktop.wayland.server.WlSeatResource;
import org.freedesktop.wayland.server.WlShellSurfaceRequests;
import org.freedesktop.wayland.server.WlShellSurfaceResource;
import org.freedesktop.wayland.server.WlSurfaceResource;

import java.util.Set;

@AutoFactory(className = "WlShellSurfaceFactory")
public class WlShellSurface extends EventBus implements WlShellSurfaceRequests, ProtocolObject<WlShellSurfaceResource> {

    private final Set<WlShellSurfaceResource> resources = Sets.newHashSet();

    private final WlSurface wlSurface;

    WlShellSurface(final WlSurface wlSurface) {
        this.wlSurface = wlSurface;
    }

    @Override
    public void pong(final WlShellSurfaceResource requester,
                     final int serial) {

    }

    @Override
    public void move(final WlShellSurfaceResource requester,
                     final WlSeatResource seat,
                     final int serial) {

    }

    @Override
    public void resize(final WlShellSurfaceResource requester,
                       final WlSeatResource seat,
                       final int serial,
                       final int edges) {

    }

    @Override
    public void setToplevel(final WlShellSurfaceResource requester) {

    }

    @Override
    public void setTransient(final WlShellSurfaceResource requester,
                             final WlSurfaceResource parent,
                             final int x,
                             final int y,
                             final int flags) {

    }

    @Override
    public void setFullscreen(final WlShellSurfaceResource requester,
                              final int method,
                              final int framerate,
                              final WlOutputResource output) {

    }

    @Override
    public void setPopup(final WlShellSurfaceResource requester,
                         final WlSeatResource seat,
                         final int serial,
                         final WlSurfaceResource parent,
                         final int x,
                         final int y,
                         final int flags) {

    }

    @Override
    public void setMaximized(final WlShellSurfaceResource requester,
                             final WlOutputResource output) {

    }

    @Override
    public void setTitle(final WlShellSurfaceResource requester,
                         final String title) {

    }

    @Override
    public void setClass(final WlShellSurfaceResource requester,
                         final String class_) {

    }

    @Override
    public Set<WlShellSurfaceResource> getResources() {
        return this.resources;
    }

    @Override
    public WlShellSurfaceResource create(final Client client,
                                         final int version,
                                         final int id) {
        return new WlShellSurfaceResource(client,
                version,
                id,
                this);
    }
}

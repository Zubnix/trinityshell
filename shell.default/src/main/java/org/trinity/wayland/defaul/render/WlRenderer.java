package org.trinity.wayland.defaul.render;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.defaul.protocol.WlShmBuffer;

import javax.inject.Inject;

public class WlRenderer {

    private final Display display;

    private ShellSurface current;

    @Inject
    WlRenderer(final Display display) {
        this.display = display;
    }

    public void render(final ShellSurface shellSurface) {
        this.current = shellSurface;
        shellSurface.getBuffer()
                    .get()
                    .accept(this);
    }

    @Subscribe
    public void visit(final WlShmBuffer buffer){
        draw(buffer);
        final int serial = this.display.nextSerial();
        this.current.getPaintCallbacks().forEach(callback ->
                                                 callback.accept(serial));
    }

    private void draw(final WlShmBuffer buffer) {
        //TODO do actual render here.

    }
}

package org.trinity.wayland.defaul;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.defaul.protocol.WlShmBuffer;

import javax.inject.Inject;

public class WlShmRenderer {

    public static EventBus DISPATCHER(final WlShmBuffer wlShmBuffer){
        return new EventBus(){{
            register(new Object(){
                @Subscribe
                public void dispatch(final WlShmRenderer renderer){
                    renderer.visit(wlShmBuffer);
                }
            });
        }};
    }

    private final Display        display;
    private final WlShmRenderEngine engine;

    private ShellSurface current;

    @Inject
    WlShmRenderer(final Display display,
                  final WlShmRenderEngine engine) {
        this.display = display;
        this.engine  = engine;
    }

    public void render(final ShellSurface shellSurface) {
        this.current = shellSurface;
        shellSurface.getBuffer()
                    .get()
                    .accept(this);
    }

    @Subscribe
    public void visit(final WlShmBuffer buffer){
        this.engine.draw(this.current,
                         buffer);
        final int serial = this.display.nextSerial();
        this.current.getPaintCallbacks().forEach(callback ->
                                                 callback.accept(serial));
    }
}

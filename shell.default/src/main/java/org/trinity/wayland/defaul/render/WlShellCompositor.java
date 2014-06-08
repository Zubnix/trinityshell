package org.trinity.wayland.defaul.render;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.Committed;

import javax.inject.Inject;

public class WlShellCompositor {

    private final Display    display;
    private final WlScene    wlScene;
    private final WlRenderer wlRenderer;

    @Inject
    WlShellCompositor(final Display    display,
                      final WlScene    wlScene,
                      final WlRenderer wlRenderer) {
        this.display = display;
        this.wlScene = wlScene;
        this.wlRenderer = wlRenderer;
    }

    @Subscribe
    public void handle(final Committed event) {
        final ShellSurface shellSurface = event.getSource();

        if(shellSurface.getDamage()
                       .isPresent()) {
            requestRender(shellSurface);
        }
    }

    private void requestRender(final ShellSurface shellSurface) {
        if(this.wlScene.needsRender(shellSurface)){
            this.display.getEventLoop().addIdle(() ->
                                                this.wlScene.getShellSurfacesStack().forEach(this.wlRenderer::render));
        }
    }
}

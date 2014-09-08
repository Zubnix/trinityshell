package org.trinity.wayland;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.trinity.SimpleShellSurfaceFactory;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.Committed;

import javax.inject.Inject;
import java.util.Optional;

public class WlShellCompositor {

    private final Display                   display;
    private final WlScene                   wlScene;
    private final WlShmRenderer             wlRenderer;
    private final SimpleShellSurfaceFactory simpleShellSurfaceFactory;


    @Inject
    WlShellCompositor(final Display display,
                      final WlScene wlScene,
                      final WlShmRenderer wlRenderer,
                      final SimpleShellSurfaceFactory simpleShellSurfaceFactory) {
        this.display = display;
        this.wlScene = wlScene;
        this.wlRenderer = wlRenderer;
        this.simpleShellSurfaceFactory = simpleShellSurfaceFactory;
    }

    public ShellSurface create() {
        final ShellSurface shellSurface = this.simpleShellSurfaceFactory.create(Optional.empty());
        shellSurface.register(this);
        this.wlScene.add(shellSurface);
        return shellSurface;
    }

    @Subscribe
    public void handle(final Committed event) {
        final ShellSurface shellSurface = event.getSource();

        if (shellSurface.getDamage()
                        .isPresent()) {
            requestRender(shellSurface);
        }
    }

    private void requestRender(final ShellSurface shellSurface) {
        if (this.wlScene.needsRender(shellSurface)) {
            this.display.getEventLoop()
                        .addIdle(() ->
                                         this.wlScene.getShellSurfacesStack()
                                                     .forEach(this.wlRenderer::render));
        }
    }
}

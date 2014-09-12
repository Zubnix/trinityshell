package org.trinity.wayland;

import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.trinity.SimpleShellSurfaceFactory;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.Committed;
import org.trinity.shell.scene.api.event.Destroyed;

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
        this.wlScene.getShellSurfacesStack()
                    .add(shellSurface);
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

    @Subscribe
    public void handle(final Destroyed event) {
        final ShellSurface shellSurface = event.getSource();
        this.wlScene.getShellSurfacesStack()
                    .remove(shellSurface);
        renderScene();
    }

    private void requestRender(final ShellSurface shellSurface) {
        if (this.wlScene.needsRender(shellSurface)) {
            renderScene();
        }
    }

    private void renderScene() {
        this.display.getEventLoop()
                    .addIdle(() -> {
                        this.wlScene.getShellSurfacesStack()
                                    .forEach(this.wlRenderer::render);
                        this.display.flushClients();
                    });
    }
}

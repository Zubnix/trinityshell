package org.trinity.wayland.output;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.Committed;
import org.trinity.shell.scene.api.event.Destroyed;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@AutoFactory(className = "CompositorFactory")
public class Compositor {

    private final Display                               display;
    private final Scene                                 scene;
    private final ShmRenderer                           wlRenderer;
    private final org.trinity.SimpleShellSurfaceFactory simpleShellSurfaceFactory;

    @Inject
    Compositor(@Provided final Display display,
               @Provided final Scene scene,
               final ShmRenderer wlRenderer,
               @Provided final org.trinity.SimpleShellSurfaceFactory simpleShellSurfaceFactory) {
        this.display = display;
        this.scene = scene;
        this.wlRenderer = wlRenderer;
        this.simpleShellSurfaceFactory = simpleShellSurfaceFactory;
    }

    public ShellSurface create() {
        final ShellSurface shellSurface = this.simpleShellSurfaceFactory.create(Optional.empty());
        shellSurface.register(this);
        this.scene.getShellSurfacesStack()
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
        this.scene.getShellSurfacesStack()
                    .remove(shellSurface);
        renderScene();
    }

    private void requestRender(final ShellSurface shellSurface) {
        if (this.scene.needsRender(shellSurface)) {
            renderScene();
        }
    }

    private void renderScene() {
        this.display.getEventLoop()
                    .addIdle(() -> {
                        try {
                            this.wlRenderer.beginRender();
                            this.scene.getShellSurfacesStack()
                                      .forEach(this.wlRenderer::render);
                            this.wlRenderer.endRender();
                            this.display.flushClients();
                        }
                        catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
    }

    public Scene getScene() {
        return this.scene;
    }
}

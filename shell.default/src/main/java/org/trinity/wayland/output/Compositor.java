package org.trinity.wayland.output;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.Committed;
import org.trinity.shell.scene.api.event.Destroyed;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

@AutoFactory(className = "CompositorFactory")
public class Compositor {

    private final Display                               display;
    private final Scene                                 scene;
    private final ShmRenderer                           shmRenderer;
    private final org.trinity.SimpleShellSurfaceFactory simpleShellSurfaceFactory;

    private AtomicBoolean renderScheduled = new AtomicBoolean(false);

    Compositor(@Provided final Display display,
               @Provided final Scene scene,
               final ShmRenderer shmRenderer,
               @Provided final org.trinity.SimpleShellSurfaceFactory simpleShellSurfaceFactory) {
        this.display = display;
        this.scene = scene;
        this.shmRenderer = shmRenderer;
        this.simpleShellSurfaceFactory = simpleShellSurfaceFactory;
    }

    public ShellSurface create() {
        final ShellSurface shellSurface = this.simpleShellSurfaceFactory.create(Optional.empty());
        shellSurface.register(this);
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
        if (this.scene.getShellSurfacesStack()
                      .remove(event.getSource())) {
            renderScene();
        }
    }

    public void requestRender(final ShellSurface shellSurface) {
        if (this.renderScheduled.compareAndSet(false,
                                               true)) {
            if (this.scene.needsRender(shellSurface)) {
                renderScene();
            }
        }
    }

    private void renderScene() {
        this.display.getEventLoop()
                    .addIdle(() -> {
                        if (this.renderScheduled.compareAndSet(true,
                                                               false)) {
                            try {
                                this.shmRenderer.beginRender();
                                this.scene.getShellSurfacesStack()
                                          .forEach(this.shmRenderer::render);
                                this.shmRenderer.endRender();
                                this.display.flushClients();
                            }
                            catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
    }

    public Scene getScene() {
        return this.scene;
    }
}

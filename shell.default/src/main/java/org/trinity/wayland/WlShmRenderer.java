package org.trinity.wayland;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.ShmBuffer;
import org.freedesktop.wayland.server.WlBufferResource;
import org.trinity.shell.scene.api.ShellSurface;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WlShmRenderer {

    private final EventBus dispatcher = new EventBus();

    private final Display           display;
    private final WlShmRenderEngine engine;

    private ShellSurface current;

    @Inject
    WlShmRenderer(final Display display,
                  final WlShmRenderEngine engine) {
        this.display = display;
        this.engine = engine;

        this.dispatcher.register(this);
    }

    public void render(final ShellSurface shellSurface) {
        this.current = shellSurface;
        dispatcher.post(shellSurface.getBuffer()
                                    .get());
    }

    @Subscribe
    public void unknownBufferType(final DeadEvent deadEvent) {
        throw new IllegalArgumentException(String.format("Buffer %s is not a known type.",
                                                         deadEvent.getEvent()
                                                                  .getClass()
                                                                  .getName()));
    }

    @Subscribe
    public void render(final WlBufferResource bufferResource) throws ExecutionException, InterruptedException {

        final ShmBuffer shmBuffer = ShmBuffer.get(bufferResource);
        if (shmBuffer == null) {
            throw new IllegalArgumentException("Buffer is not an ShmBuffer.");
        }

        this.engine.draw(this.current,
                         shmBuffer)
                   .get();
        this.current.firePaintCallbacks((int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime()));
    }

    public void beginRender() throws ExecutionException, InterruptedException {
        this.engine.begin()
                   .get();
    }

    public void endRender() throws ExecutionException, InterruptedException {
        this.engine.end()
                   .get();
    }
}

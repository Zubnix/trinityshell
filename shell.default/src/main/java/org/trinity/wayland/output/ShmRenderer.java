package org.trinity.wayland.output;

import com.google.auto.factory.AutoFactory;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.freedesktop.wayland.server.ShmBuffer;
import org.freedesktop.wayland.server.WlBufferResource;
import org.freedesktop.wayland.server.WlSurfaceRequests;
import org.freedesktop.wayland.server.WlSurfaceResource;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.protocol.WlSurface;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@AutoFactory(className = "ShmRendererFactory")
public class ShmRenderer {

    private final EventBus dispatcher = new EventBus();

    private final ShmRenderEngine shmRenderEngine;

    private ShellSurface current;

    ShmRenderer(final ShmRenderEngine shmRenderEngine) {
        this.shmRenderEngine = shmRenderEngine;
        this.dispatcher.register(this);
    }

    public void render(final WlSurfaceResource surfaceResource) {
        final WlSurfaceRequests implementation = surfaceResource.getImplementation();

        this.current = ((WlSurface) implementation).getShellSurface();
        this.dispatcher.post(this.current.getBuffer()
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
    public void render(final WlBufferResource bufferResource) {

        final ShmBuffer shmBuffer = ShmBuffer.get(bufferResource);
        if (shmBuffer == null) {
            throw new IllegalArgumentException("Buffer is not an ShmBuffer.");
        }
        try {
            this.shmRenderEngine.draw(this.current,
                                      shmBuffer)
                                .get();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        this.current.firePaintCallbacks((int) TimeUnit.NANOSECONDS.toMillis(System.nanoTime()));
    }

    public void beginRender() throws ExecutionException, InterruptedException {
        this.shmRenderEngine.begin()
                            .get();
    }

    public void endRender() throws ExecutionException, InterruptedException {
        this.shmRenderEngine.end()
                            .get();
    }
}

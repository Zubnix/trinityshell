package org.trinity.shell.surface.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.shell.api.scene.ShellScene;
import org.trinity.shell.api.surface.ShellSurface;
import org.trinity.shell.api.surface.ShellSurfaceFactory;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.devsurf.injection.guice.annotations.Bind;

@Bind
@ThreadSafe
public class ShellSurfaceFactoryImpl implements ShellSurfaceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ShellSurfaceFactory.class);

    private final ShellScene shellScene;
    private final ListeningExecutorService shellExecutor;

    @Inject
    ShellSurfaceFactoryImpl(final ShellScene shellScene,
                            @Named("Shell") final ListeningExecutorService shellExecutor) {
        this.shellScene = shellScene;
        this.shellExecutor = shellExecutor;
    }

    @Override
    public ListenableFuture<ShellSurface> createShellClientSurface(final DisplaySurface displaySurface) {
        return this.shellExecutor.submit(new Callable<ShellSurface>() {
            @Override
            public ShellSurface call() {
                final ShellClientSurface shellClientSurface = new ShellClientSurface(shellScene,
                        shellExecutor,
                        displaySurface);
                syncGeoToDisplaySurfaceImpl(displaySurface, shellClientSurface);
                displaySurface.register(shellClientSurface,
                        shellExecutor);

                return null;
            }
        });
    }

    private void syncGeoToDisplaySurfaceImpl(final DisplaySurface displaySurface, final ShellClientSurface shellClientSurface) {
        try {
            //we need to block/sync here because we dont want to expose an incomplete shellclientsurface object to other threads.
            final Rectangle displaySurfaceGeo = displaySurface.getGeometry().get();
            shellClientSurface.setPositionImpl(displaySurfaceGeo.getPosition());
            shellClientSurface.setSizeImpl(displaySurfaceGeo.getSize());
            shellClientSurface.flushSizePlaceValues();
        } catch (final InterruptedException e) {
            LOG.error("Interrupted while waiting for display surface geometry.",
                    e);
        } catch (final ExecutionException e) {
            LOG.error("Exception while getting display surface geometry.",
                    e);
        }
    }
}

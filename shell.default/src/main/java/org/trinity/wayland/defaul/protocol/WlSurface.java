package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_surface;
import org.freedesktop.wayland.server.Resource;
import org.trinity.common.Listenable;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;
import org.trinity.wayland.defaul.protocol.events.ResourceDestroyed;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.media.nativewindow.util.Rectangle;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory
public class WlSurface extends EventBus implements wl_surface.Requests3, Listenable {

    private final ShellSurface shellSurface;

    WlSurface(@Provided final ShellSurface shellSurface) {
        this.shellSurface = shellSurface;
    }

    public ShellSurface getShellSurface() {
        return this.shellSurface;
    }

    @Override
    public void setBufferScale(final wl_surface.Resource    resource,
                               final int                    scale) {

    }

    @Override
    public void setBufferTransform(final wl_surface.Resource    resource,
                                   final int                    transform) {

    }

    @Override
    public void destroy(final wl_surface.Resource resource) {
        post(new ResourceDestroyed(resource));
        getShellSurface().accept(ShellSurfaceConfigurable::markDestroyed);
        resource.destroy();
    }

    @Override
    public void attach(final wl_surface.Resource    resource,
                       @Nullable final Resource     buffer,
                       final int                    x,
                       final int                    y) {

        if(buffer == null){
            getShellSurface().accept(ShellSurfaceConfigurable::detachBuffer);
        }else {
            final WlShmBuffer wlShmBuffer = (WlShmBuffer) buffer.getImplementation();
            getShellSurface().accept(shellSurfaceConfigurable ->
                                     shellSurfaceConfigurable.attachBuffer(wlShmBuffer,
                                                                           x,
                                                                           y));
        }
    }

    @Override
    public void damage(final wl_surface.Resource    resource,
                       final int                    x,
                       final int                    y,
                       @Nonnegative final int       width,
                       @Nonnegative final int       height) {
        checkArgument(width > 0);
        checkArgument(height > 0);

        getShellSurface().accept(shellSurfaceConfigurable ->
                                 shellSurfaceConfigurable.markDamaged(new Rectangle(x,
                                                                                    y,
                                                                                    width,
                                                                                    height)));
    }

    @Override
    public void frame(final wl_surface.Resource resource,
                      final int                 callback) {

    }

    @Override
    public void setOpaqueRegion(final wl_surface.Resource resource,
                                final Resource            region) {
        final WlRegion wlRegion = (WlRegion) region.getImplementation();

    }

    @Override
    public void setInputRegion(final wl_surface.Resource resource,
                               @Nullable final Resource  region) {
        final WlRegion wlRegion = (WlRegion) region.getImplementation();

        getShellSurface().accept(new ShellSurfaceConfiguration() {
            @Override
            public void visit(@Nonnull ShellSurfaceConfigurable shellSurfaceConfigurable) {

            }
        });

    }

    @Override
    public void commit(final wl_surface.Resource resource) {
        getShellSurface().accept(ShellSurfaceConfigurable::commit);
    }
}

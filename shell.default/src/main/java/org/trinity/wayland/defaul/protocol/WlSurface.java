package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.ejml.data.FixedMatrix3x3_64F;
import org.freedesktop.wayland.protocol.wl_callback;
import org.freedesktop.wayland.protocol.wl_output;
import org.freedesktop.wayland.protocol.wl_surface;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;
import org.trinity.wayland.defaul.events.ResourceDestroyed;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.media.nativewindow.util.Rectangle;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlSurfaceFactory")
public class WlSurface extends EventBus implements wl_surface.Requests3, ProtocolObject<wl_surface.Resource> {

    private final Set<wl_surface.Resource> resources         = Sets.newHashSet();
    private final Object                   destroyedListener = new Object(){
        @Subscribe
        public void handle(final ResourceDestroyed resourceDestroyed){
            WlSurface.this.pendingBuffer.get().unregister(this);
            detachBuffer();
        }
    };

    private final WlCallbackFactory wlCallbackFactory;
    private final ShellSurface      shellSurface;

    private Optional<WlShmBuffer> pendingBuffer = Optional.empty();

    WlSurface(@Provided final WlCallbackFactory wlCallbackFactory,
              final ShellSurface                shellSurface) {
        this.wlCallbackFactory = wlCallbackFactory;
        this.shellSurface      = shellSurface;
    }

    public ShellSurface getShellSurface() {
        return this.shellSurface;
    }

    @Override
    public void setBufferScale(final wl_surface.Resource resource,
                               final int                 scale) {

    }

    @Override
    public void setBufferTransform(final wl_surface.Resource resource,
                                   final int                 transform) {
        this.shellSurface.accept(shellSurfaceConfigurable ->
                                 shellSurfaceConfigurable.setTransform(getMatrix(transform)));
    }

    private FixedMatrix3x3_64F getMatrix(final int transform){
        switch (transform) {
            case wl_output.TRANSFORM_NORMAL:
            case wl_output.TRANSFORM_90:
            case wl_output.TRANSFORM_180:
            case wl_output.TRANSFORM_270:
            case wl_output.TRANSFORM_FLIPPED:
            case wl_output.TRANSFORM_FLIPPED_90:
            case wl_output.TRANSFORM_FLIPPED_180:
            case wl_output.TRANSFORM_FLIPPED_270:
                return new FixedMatrix3x3_64F(1,0,0,
                                              0,1,0,
                                              0,0,1);
            default:
                throw new IllegalArgumentException("Invalid transform");
        }
    }

    @Override
    public Set<wl_surface.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_surface.Resource create(final Client client,
                                      final int    version,
                                      final int    id) {
        return new wl_surface.Resource(client,
                                       version,
                                       id);
    }


    @Override
    public void destroy(final wl_surface.Resource resource) {
        ProtocolObject.super.destroy(resource);
        getShellSurface().accept(ShellSurfaceConfigurable::markDestroyed);
    }

    @Override
    public void attach(final wl_surface.Resource resource,
                       @Nullable final Resource  bufferResource,
                       final int                 x,
                       final int                 y) {
        if(bufferResource == null){
            detachBuffer();
        }
        else {
            attachBuffer((WlShmBuffer) bufferResource.getImplementation(),
                         x,
                         y);
        }
    }

    @Override
    public void damage(final wl_surface.Resource resource,
                       final int                 x,
                       final int                 y,
                       @Nonnegative final int    width,
                       @Nonnegative final int    height) {
        checkArgument(width  > 0);
        checkArgument(height > 0);

        getShellSurface().accept(shellSurfaceConfigurable ->
                                 shellSurfaceConfigurable.markDamaged(new Rectangle(x,
                                                                                    y,
                                                                                    width,
                                                                                    height)));
    }

    @Override
    public void frame(final wl_surface.Resource resource,
                      final int                 callbackId) {
        final wl_callback.Resource callbackResource = this.wlCallbackFactory.create().add(resource.getClient(),
                                                                                          1,
                                                                                          callbackId);
        getShellSurface().accept(shellSurfaceConfigurable ->
                                 shellSurfaceConfigurable.addCallback(serial -> {
                                     callbackResource.done(serial);
                                     callbackResource.destroy();
                                 }));
    }

    @Override
    public void setOpaqueRegion(final wl_surface.Resource resource,
                                @Nullable final Resource  regionResource) {
        if(regionResource == null) {
            getShellSurface().accept(ShellSurfaceConfigurable::removeOpaqueRegion);
        }
        else {
            final WlRegion wlRegion = (WlRegion) regionResource.getImplementation();
            getShellSurface().accept(shellSurfaceConfigurable ->
                                     shellSurfaceConfigurable.setOpaqueRegion(wlRegion.getRegion()));
        }
    }

    @Override
    public void setInputRegion(final wl_surface.Resource surfaceResource,
                               @Nullable final Resource  regionResource) {
        if(regionResource == null){
            getShellSurface().accept(ShellSurfaceConfigurable::removeInputRegion);
        }
        else {
            final WlRegion wlRegion = (WlRegion) regionResource.getImplementation();
            getShellSurface().accept(shellSurfaceConfigurable ->
                                     shellSurfaceConfigurable.setInputRegion(wlRegion.getRegion()));
        }
    }

    @Override
    public void commit(final wl_surface.Resource resource) {
        this.pendingBuffer = Optional.empty();
        getShellSurface().accept(ShellSurfaceConfigurable::commit);
    }

    private void detachBuffer(){
        getShellSurface().accept(ShellSurfaceConfigurable::detachBuffer);
    }

    private void attachBuffer(final WlShmBuffer newBuffer,
                              final int         x,
                              final int         y){
        this.pendingBuffer.ifPresent(wlShmBuffer ->
                                     wlShmBuffer.unregister(this.destroyedListener));
        this.pendingBuffer = Optional.of(newBuffer);
        newBuffer.register(this.destroyedListener);

        getShellSurface().accept(shellSurfaceConfigurable ->
                                 shellSurfaceConfigurable.attachBuffer(newBuffer,
                                                                       x,
                                                                       y));
    }
}

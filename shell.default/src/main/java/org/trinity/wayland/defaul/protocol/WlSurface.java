package org.trinity.wayland.defaul.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.protocol.wl_callback;
import org.freedesktop.wayland.protocol.wl_surface;
import org.freedesktop.wayland.server.Client;
import org.freedesktop.wayland.server.Resource;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.media.nativewindow.util.Rectangle;

import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlSurfaceFactory")
public class WlSurface extends EventBus implements wl_surface.Requests3, ProtocolObject<wl_surface.Resource> {

    private final Set<wl_surface.Resource> resources = Sets.newHashSet();

    private final WlCallbackFactory wlCallbackFactory;
    private final ShellSurface      shellSurface;

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

    }

    @Override
    public Set<wl_surface.Resource> getResources() {
        return this.resources;
    }

    @Override
    public wl_surface.Resource create(final Client client,
                                      final int version,
                                      final int id) {
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
            getShellSurface().accept(ShellSurfaceConfigurable::detachBuffer);
        }else {
            final WlShmBuffer wlShmBuffer = (WlShmBuffer) bufferResource.getImplementation();
            getShellSurface().accept(shellSurfaceConfigurable ->
                                     shellSurfaceConfigurable.attachBuffer(wlShmBuffer,
                                                                           x,
                                                                           y));
        }
    }

    @Override
    public void damage(final wl_surface.Resource resource,
                       final int                 x,
                       final int                 y,
                       @Nonnegative final int    width,
                       @Nonnegative final int    height) {
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
                      final int                 callbackId) {
        final wl_callback.Resource callbackResource = new wl_callback.Resource(resource.getClient(),
                                                                               1,
                                                                               callbackId);
        callbackResource.setImplementation(this.wlCallbackFactory.create());
        getShellSurface().accept(shellSurfaceConfigurable ->
                                 shellSurfaceConfigurable.addPaintCallback(callbackResource::done));
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
        getShellSurface().accept(ShellSurfaceConfigurable::commit);
    }
}

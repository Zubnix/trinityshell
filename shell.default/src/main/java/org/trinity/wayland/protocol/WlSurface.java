package org.trinity.wayland.protocol;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.freedesktop.wayland.server.*;
import org.freedesktop.wayland.shared.WlOutputTransform;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfigurable;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.media.nativewindow.util.Rectangle;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by Erik De Rijcke on 5/23/14.
 */
@AutoFactory(className = "WlSurfaceFactory")
public class WlSurface extends EventBus implements WlSurfaceRequestsV3, ProtocolObject<WlSurfaceResource> {

    public static final Map<ShellSurface,WlSurface> SHELL_SURFACE_WL_SURFACE_MAP = new WeakHashMap<>();

    private final Set<WlSurfaceResource> resources       = Sets.newHashSet();
    private final Listener               destroyListener = new Listener() {
        @Override
        public void handle() {
            detachBuffer();
            shellSurface.accept(ShellSurfaceConfigurable::detachBuffer);
            destroy();
        }
    };

    private final WlCallbackFactory wlCallbackFactory;
    private final ShellSurface      shellSurface;

    private Optional<WlBufferResource> pendingBuffer = Optional.empty();

    WlSurface(
            @Provided
            final WlCallbackFactory wlCallbackFactory,
            final ShellSurface shellSurface) {
        this.wlCallbackFactory = wlCallbackFactory;
        this.shellSurface = shellSurface;
        SHELL_SURFACE_WL_SURFACE_MAP.put(shellSurface,this);
    }

    public ShellSurface getShellSurface() {
        return this.shellSurface;
    }

    @Override
    public void setBufferScale(final WlSurfaceResource resource,
                               final int scale) {

    }

    @Override
    public void setBufferTransform(final WlSurfaceResource resource,
                                   final int transform) {
        this.shellSurface.accept(shellSurfaceConfigurable ->
                                         shellSurfaceConfigurable.setTransform(getMatrix(transform)));
    }

    private float[] getMatrix(final int transform) {
        if (transform == WlOutputTransform.FLIPPED_270.getValue()) {
            return new float[]{1, 0, 0,
                               0, 1, 0,
                               0, 0, 1};
        }
        throw new IllegalArgumentException("Invalid transform");
    }

    @Override
    public Set<WlSurfaceResource> getResources() {
        return this.resources;
    }

    @Override
    public WlSurfaceResource create(final Client client,
                                    final int version,
                                    final int id) {
        return new WlSurfaceResource(client,
                                     version,
                                     id,
                                     this);
    }


    @Override
    public void destroy(final WlSurfaceResource resource) {
        ProtocolObject.super.destroy(resource);
        getShellSurface().accept(ShellSurfaceConfigurable::markDestroyed);
    }

    @Override
    public void attach(final WlSurfaceResource requester,
                       @Nullable
                       final WlBufferResource buffer,
                       final int x,
                       final int y) {
        if (buffer == null) {
            detachBuffer();
        }
        else {
            attachBuffer(buffer,
                         x,
                         y);
        }
    }

    @Override
    public void damage(final WlSurfaceResource resource,
                       final int x,
                       final int y,
                       @Nonnegative
                       final int width,
                       @Nonnegative
                       final int height) {
        checkArgument(width > 0);
        checkArgument(height > 0);

        getShellSurface().accept(shellSurfaceConfigurable ->
                                         shellSurfaceConfigurable.markDamaged(new Rectangle(x,
                                                                                            y,
                                                                                            width,
                                                                                            height)));
    }

    @Override
    public void frame(final WlSurfaceResource resource,
                      final int callbackId) {
        final WlCallbackResource callbackResource = this.wlCallbackFactory.create()
                                                                          .add(resource.getClient(),
                                                                               resource.getVersion(),
                                                                               callbackId);
        getShellSurface().accept(shellSurfaceConfigurable ->
                                         shellSurfaceConfigurable.addCallback(serial -> {
                                             callbackResource.done(serial);
                                             callbackResource.destroy();
                                         }));
    }

    @Override
    public void setOpaqueRegion(final WlSurfaceResource requester,
                                final WlRegionResource region) {
        if (region == null) {
            getShellSurface().accept(ShellSurfaceConfigurable::removeOpaqueRegion);
        }
        else {
            final WlRegion wlRegion = (WlRegion) region.getImplementation();
            getShellSurface().accept(shellSurfaceConfigurable ->
                                             shellSurfaceConfigurable.setOpaqueRegion(wlRegion.getRegion()));
        }
    }

    @Override
    public void setInputRegion(final WlSurfaceResource requester,
                               @Nullable
                               final WlRegionResource regionResource) {
        if (regionResource == null) {
            getShellSurface().accept(ShellSurfaceConfigurable::removeInputRegion);
        }
        else {
            final WlRegion wlRegion = (WlRegion) regionResource.getImplementation();
            getShellSurface().accept(shellSurfaceConfigurable ->
                                             shellSurfaceConfigurable.setInputRegion(wlRegion.getRegion()));
        }
    }

    @Override
    public void commit(final WlSurfaceResource requester) {
        this.pendingBuffer = Optional.empty();
        this.destroyListener.remove();
        getShellSurface().accept(ShellSurfaceConfigurable::commit);
    }

    private void detachBuffer() {
        getShellSurface().accept(ShellSurfaceConfigurable::detachBuffer);
    }

    private void attachBuffer(final WlBufferResource buffer,
                              final int x,
                              final int y) {
        this.pendingBuffer.ifPresent(wlShmBuffer ->
                                             this.destroyListener.remove());
        this.pendingBuffer = Optional.of(buffer);
        buffer.addDestroyListener(this.destroyListener);

        getShellSurface().accept(shellSurfaceConfigurable ->
                                         shellSurfaceConfigurable.attachBuffer(buffer,
                                                                               x,
                                                                               y));
    }
}

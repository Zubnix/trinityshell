package org.trinity.x11.defaul.shell;

import com.google.common.eventbus.EventBus;
import org.trinity.x11.defaul.XWindow;
import org.trinity.shell.scene.api.BufferSpace;
import org.trinity.shell.scene.api.HasSize;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.ShellSurfaceConfiguration;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;

@Singleton
public class SimpleRootShellSurface extends EventBus implements ShellSurface {

    private final PointImmutable position = new Point(0,
                                                      0);
    private final XWindow rootXWindow;

    @Inject
    SimpleRootShellSurface(final XWindow rootXWindow) {
        this.rootXWindow = rootXWindow;
    }

    @Nonnull
    @Override
    public ShellSurface accept(@Nonnull final ShellSurfaceConfiguration shellSurfaceConfiguration) {
        //TODO implement with xrandr thingy?
        //shellSurfaceConfiguration.setShape(...);
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Nonnull
    @Override
    public PointImmutable getPosition() {
        return this.position;
    }

    @Nonnull
    @Override
    public HasSize<BufferSpace> getBuffer() {
        return this.rootXWindow;
    }

    @Nonnull
    @Override
    public Boolean isVisible() {
        return true;
    }

    @Nonnull
    @Override
    public Boolean isDestroyed() {
        return false;
    }

    @Nonnull
    @Override
    public ShellSurface requestLower() {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public ShellSurface requestMove(final int x,
                                    final int y) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public ShellSurface requestRaise() {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public ShellSurface requestReparent(@Nonnull final ShellSurface parent) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public ShellSurface requestResize(@Nonnegative final int width,
                                      @Nonnegative final int height) {
        //TODO delegate to xrandr thingy?
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Nonnull
    @Override
    public ShellSurface requestShow() {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public ShellSurface requestHide() {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public ShellSurface getParent() {
        return this;
    }

    @Nonnull
    @Override
    public DimensionImmutable getSize() {
        return this.rootXWindow.getSize();
    }
}

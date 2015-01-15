package org.trinity.x11.shell;

import com.google.common.eventbus.Subscribe;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.nativewindow.util.PointImmutable;
import java.util.LinkedList;

@Singleton
public class XScene {

    private final LinkedList<ShellSurface> shellSurfacesStack = new LinkedList<>();

    @Inject
    XScene() {}

    public LinkedList<ShellSurface> getShellSurfacesStack() { return this.shellSurfacesStack; }

    public void add(final ShellSurface shellSurface) {
        shellSurface.register(this);
        this.shellSurfacesStack.addLast(shellSurface);
    }

    public boolean needsRedraw(final ShellSurface shellSurface) {
        //for now, always redraw
        return true;
    }

    @Subscribe
    public void handle(final MoveRequest moveRequest) {
        final PointImmutable position = moveRequest.getPosition();
        moveRequest.getSource()
                   .accept(shellSurfaceConfigurable ->
                                   shellSurfaceConfigurable.setPosition(position));
    }

    @Subscribe
    public void handle(final LowerRequest lowerRequest) {
        final ShellSurface shellSurface = lowerRequest.getSource();
        this.shellSurfacesStack.remove(shellSurface);
        this.shellSurfacesStack.addFirst(shellSurface);
        shellSurface.post(new Lowered(shellSurface));
    }

    @Subscribe
    public void handle(final RaiseRequest raiseRequest) {
        final ShellSurface shellSurface = raiseRequest.getSource();
        this.shellSurfacesStack.remove(shellSurface);
        this.shellSurfacesStack.addLast(shellSurface);
        shellSurface.post(new Raised(shellSurface));
    }

    @Subscribe
    public void handle(final Destroyed destroyed) {
        final ShellSurface shellSurface = destroyed.getSource();
        shellSurface.unregister(this);
        this.shellSurfacesStack.remove(shellSurface);
    }
}

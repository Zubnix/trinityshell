package org.trinity.x11.defaul.shell;

import com.google.common.eventbus.Subscribe;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.shell.scene.api.event.Destroyed;
import org.trinity.shell.scene.api.event.LowerRequest;
import org.trinity.shell.scene.api.event.Lowered;
import org.trinity.shell.scene.api.event.MoveRequest;
import org.trinity.shell.scene.api.event.RaiseRequest;
import org.trinity.shell.scene.api.event.Raised;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.nativewindow.util.PointImmutable;
import java.util.LinkedList;

@Singleton
public class SimpleShell {

    private final LinkedList<ShellSurface> shellSurfacesStack = new LinkedList<>();

    @Inject
    SimpleShell() {}

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
                   .accept(shellSurfaceConfigurable -> {
                       shellSurfaceConfigurable.setPosition(position);
                   });
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

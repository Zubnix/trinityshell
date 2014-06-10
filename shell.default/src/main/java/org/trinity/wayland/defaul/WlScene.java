package org.trinity.wayland.defaul;

import com.google.common.collect.Lists;
import org.trinity.shell.scene.api.ShellSurface;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;

@Singleton
public class WlScene {
    private final LinkedList<ShellSurface> shellSurfacesStack = Lists.newLinkedList();

    @Inject
    WlScene() {
    }

    public LinkedList<ShellSurface> getShellSurfacesStack() { return this.shellSurfacesStack; }

    public void add(final ShellSurface shellSurface) {
        shellSurface.register(this);
        this.shellSurfacesStack.addLast(shellSurface);
    }

    public boolean needsRender(final ShellSurface shellSurface) {
        //for now, always redraw
        return true;
    }
}

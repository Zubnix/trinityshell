package org.trinity.wayland;

import com.google.common.collect.Lists;
import org.trinity.shell.scene.api.ShellSurface;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.Optional;

@Singleton
public class WlScene {
    private final LinkedList<ShellSurface> shellSurfacesStack = Lists.newLinkedList();

    @Inject
    WlScene() {
    }

    public LinkedList<ShellSurface> getShellSurfacesStack() { return this.shellSurfacesStack; }

    public Optional<ShellSurface> findSurfaceAtCoordinate(int absX, int absY){
        //TODO find underlying shellsurface
        return null;
    }

    public boolean needsRender(final ShellSurface shellSurface) {
        //for now, always redraw
        return true;
    }
}

package org.trinity.wayland.output;

import com.google.common.collect.Lists;
import org.trinity.shell.scene.api.Region;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.protocol.WlSurface;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.media.nativewindow.util.Point;
import javax.media.nativewindow.util.PointImmutable;
import javax.media.nativewindow.util.RectangleImmutable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

@Singleton
public class Scene {
    private final LinkedList<WlSurface> shellSurfacesStack = Lists.newLinkedList();

    @Inject
    Scene() {
    }

    public LinkedList<WlSurface> getShellSurfacesStack() { return this.shellSurfacesStack; }

    public PointImmutable relativeCoordinate(final WlSurface wlSurface,
                                             final int absX,
                                             final int absY) {
        final PointImmutable position = wlSurface.getShellSurface()
                                                 .getPosition();
        final int offsetX = position.getX();
        final int offsetY = position.getY();
        return new Point(absX - offsetX,
                         absY - offsetY);
    }

    public Optional<WlSurface> findSurfaceAtCoordinate(final int absX,
                                                       final int absY) {
        final Iterator<WlSurface> shellSurfaceIterator = getShellSurfacesStack().descendingIterator();

        while (shellSurfaceIterator.hasNext()) {
            final WlSurface wlSurface = shellSurfaceIterator.next();
            final ShellSurface shellSurface = wlSurface.getShellSurface();

            final Optional<Region> inputRegion = shellSurface.getInputRegion();
            if (inputRegion.isPresent()) {

                final PointImmutable position = shellSurface.getPosition();
                final int offsetX = position.getX();
                final int offsetY = position.getY();

                for (final RectangleImmutable rectangle : inputRegion.get()
                                                                     .asList()) {
                    final int x1 = rectangle.getX() + offsetX;
                    final int y1 = rectangle.getY() + offsetY;

                    final int x2 = x1 + rectangle.getWidth();
                    final int y2 = y1 + rectangle.getHeight();

                    if (x1 <= absX && x1 <= x2 && y1 <= absY && absY <= y2) {
                        return Optional.of(wlSurface);
                    }
                }
            }
        }

        return Optional.empty();
    }

    public boolean needsRender(final ShellSurface shellSurface) {
        if(shellSurface.isDestroyed()){
            return true;
        }else {
            //for now, always redraw
            return true;
        }
    }
}

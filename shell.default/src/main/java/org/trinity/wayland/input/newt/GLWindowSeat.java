package org.trinity.wayland.input.newt;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import org.freedesktop.wayland.server.Display;
import org.freedesktop.wayland.server.WlPointerResource;
import org.freedesktop.wayland.server.WlSurfaceResource;
import org.freedesktop.wayland.util.Fixed;
import org.trinity.shell.scene.api.ShellSurface;
import org.trinity.wayland.WlJobExecutor;
import org.trinity.wayland.WlShellCompositor;
import org.trinity.wayland.protocol.WlPointer;
import org.trinity.wayland.protocol.WlSeat;
import org.trinity.wayland.protocol.WlSurface;

import javax.media.nativewindow.util.PointImmutable;
import java.util.Optional;

public class GLWindowSeat implements MouseListener, KeyListener {

    private final Display           display;
    private final WlSeat            wlSeat;
    private final WlShellCompositor wlShellCompositor;
    private final WlJobExecutor     wlJobExecutor;

    private Optional<WlSurface> currentFocus = Optional.empty();

    public GLWindowSeat(Display display,
                        WlSeat wlSeat,
                        WlShellCompositor wlShellCompositor,
                        WlJobExecutor wlJobExecutor) {
        this.display = display;
        this.wlSeat = wlSeat;
        this.wlShellCompositor = wlShellCompositor;
        this.wlJobExecutor = wlJobExecutor;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseMoved(final MouseEvent e) {

        final long time = e.getWhen();
        final int absX = e.getX();
        final int absY = e.getY();

        this.wlJobExecutor.submit(() -> handlePointerMove(time,
                                                          absX,
                                                          absY));
    }

    private void handlePointerMove(final long time,
                                   final int absX,
                                   final int absY) {
        final Optional<ShellSurface> newFocusShellSurface = this.wlShellCompositor.getWlScene()
                                                                                  .findSurfaceAtCoordinate(absX,
                                                                                                           absY);
        final Optional<WlSurface> newFocus;
        if (newFocusShellSurface.isPresent()) {
            newFocus = Optional.of(WlSurface.SHELL_SURFACE_WL_SURFACE_MAP.get(newFocusShellSurface.get()));
        }
        else {
            newFocus = Optional.empty();
        }

        if (!this.currentFocus.equals(newFocus)) {
            shiftFocus(newFocus,
                       absX,
                       absY);
        }
        else if (this.currentFocus.isPresent()) {
            reportMotion((int) time,
                         absX,
                         absY);
        }
    }


    private void reportMotion(final int time,
                              final int absX,
                              final int absY) {
        final WlSurfaceResource wlSurfaceResource = this.currentFocus.get()
                                                                     .getResource()
                                                                     .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
        if (pointerResource.isPresent()) {
            final PointImmutable relativePoint = this.wlShellCompositor.getWlScene()
                                                                       .relativeCoordinate(this.currentFocus.get()
                                                                                                            .getShellSurface(),
                                                                                           absX,
                                                                                           absY);
            pointerResource.get()
                           .motion(time,
                                   Fixed.create(relativePoint.getX()),
                                   Fixed.create(relativePoint.getY()));
        }
    }

    private void shiftFocus(Optional<WlSurface> newFocus,
                            final int absX,
                            final int absY) {

        if (this.currentFocus.isPresent()) {
            leaveFocus();
        }

        if (newFocus.isPresent()) {
            gainFocus(newFocus.get(),
                      absX,
                      absY);
        }

        this.currentFocus = newFocus;
    }

    private void gainFocus(final WlSurface newFocus,
                           final int absX,
                           final int absY) {
        //a new surface has the focus, it now gains focus
        final WlSurfaceResource wlSurfaceResource = newFocus.getResource()
                                                            .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
        if (pointerResource.isPresent()) {
            final PointImmutable relativePoint = this.wlShellCompositor.getWlScene()
                                                                       .relativeCoordinate(newFocus.getShellSurface(),
                                                                                           absX,
                                                                                           absY);

            pointerResource.get()
                           .enter(this.display.nextSerial(),
                                  wlSurfaceResource,
                                  Fixed.create(relativePoint.getX()),
                                  Fixed.create(relativePoint.getY()));
        }
    }

    private void leaveFocus() {
        final WlSurfaceResource wlSurfaceResource = this.currentFocus.get()
                                                                     .getResource()
                                                                     .get();
        final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
        if (pointerResource.isPresent()) {
            pointerResource.get()
                           .leave(this.display.nextSerial(),
                                  wlSurfaceResource);
        }
    }

    private Optional<WlPointerResource> findPointerResource(WlSurfaceResource wlSurfaceResource) {
        final WlPointer wlPointer = this.wlSeat.getOptionalWlPointer()
                                               .get();
        for (WlPointerResource wlPointerResource : wlPointer.getResources()) {
            if (wlSurfaceResource.getClient()
                                 .equals(wlPointerResource.getClient())) {
                return Optional.of(wlPointerResource);
            }
        }
        return Optional.empty();
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(final MouseEvent e) {

    }

    @Override
    public void keyPressed(final KeyEvent e) {

    }

    @Override
    public void keyReleased(final KeyEvent e) {

    }
}

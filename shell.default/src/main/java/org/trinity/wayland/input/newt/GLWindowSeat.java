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
import org.trinity.wayland.WlScene;
import org.trinity.wayland.WlShellCompositor;
import org.trinity.wayland.protocol.WlPointer;
import org.trinity.wayland.protocol.WlSeat;
import org.trinity.wayland.protocol.WlSurface;

import java.util.Optional;
import java.util.function.Consumer;

public class GLWindowSeat implements MouseListener, KeyListener{

  private final Display display;
  private final WlSeat wlSeat;
  private final WlShellCompositor wlShellCompositor;
  private final WlJobExecutor wlJobExecutor;

  private Optional<WlSurface> focusSurface = Optional.empty();

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

    //schedule a job to be executed on the compositor thread.
    this.wlJobExecutor.submit(() -> {

      final WlPointer wlPointer = wlSeat.getOptionalWlPointer().get();

      //give focus to underlying surface (if different focus surface)
      final Optional<ShellSurface> newFocusShellSurface = wlShellCompositor.getWlScene().findSurfaceAtCoordinate(absX,
                                                                                                                 absY);
      if (newFocusShellSurface.isPresent()) {
        final ShellSurface shellSurface = newFocusShellSurface.get();
        final WlSurface wlSurface = WlSurface.SHELL_SURFACE_WL_SURFACE_MAP.get(shellSurface);

        if (!focusSurface.isPresent() || !focusSurface.get().equals(wlSurface)) {
          //we have to update the focus as the pointer has reached a new surface
          final WlSurfaceResource wlSurfaceResource = wlSurface.getResource().get();
          final Optional<WlPointerResource> resourceOptional = findPointerResource(wlSurfaceResource);
          if (resourceOptional.isPresent()) {
            final WlPointerResource wlPointerResource = resourceOptional.get();
            wlPointerResource.enter(display.nextSerial(),
                                    wlSurfaceResource,
                                    //TODO calculate coordinates
                                    Fixed.create(0),
                                    Fixed.create(0));
                focusSurface = Optional.of(wlSurface);

                wlPointerResource.motion((int) time,
                                         //TODO use calculated coordinates
                                         Fixed.create(0),
                                         Fixed.create(0));
              }
        }
      }
      else if(focusSurface.isPresent()){
      }
    });
  }

  private Optional<WlPointerResource> findPointerResource(WlSurfaceResource wlSurfaceResource){
    final WlPointer wlPointer = wlSeat.getOptionalWlPointer().get();
      for (WlPointerResource wlPointerResource : wlPointer.getResources()) {
        if (wlSurfaceResource.getClient().equals(wlPointerResource.getClient())) {
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

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

import java.util.Optional;

public class GLWindowSeat implements MouseListener, KeyListener{

  private final Display display;
  private final WlSeat wlSeat;
  private final WlShellCompositor wlShellCompositor;
  private final WlJobExecutor wlJobExecutor;

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

    //schedule a job to be executed on the compositor thread.
    this.wlJobExecutor.submit(() -> {
      final Optional<ShellSurface> newFocusShellSurface = wlShellCompositor.getWlScene().findSurfaceAtCoordinate(absX,
                                                                                                                 absY);
      final Optional<WlSurface> newFocus;
      if (newFocusShellSurface.isPresent()) {
          newFocus = Optional.of(WlSurface.SHELL_SURFACE_WL_SURFACE_MAP.get(newFocusShellSurface.get()));
      }else{
          newFocus = Optional.empty();
      }

      shiftFocus(currentFocus,
                 newFocus,
                 absX,
                 absY);
      reportMotion(currentFocus,
                   (int) time,
                   absX,
                   absY);
    });
  }

  private void reportMotion(Optional<WlSurface> currentFocus,
                            final int time,
                            final int absX,
                            final int absY) {
    if(currentFocus.isPresent()) {
      final WlSurfaceResource wlSurfaceResource = currentFocus.get().getResource().get();
      final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
      if(pointerResource.isPresent()){
        pointerResource.get().motion(time,
                                     //TODO calculate relative coordinates
                                     Fixed.create(0),
                                     Fixed.create(0));
      }
    }
  }

  private void shiftFocus(Optional<WlSurface> oldFocus, Optional<WlSurface> newFocus,
                          final int absX,
                          final int absY){
    if(oldFocus.equals(newFocus)){
      //no focus shift, do nothing
      return;
    }

    if(oldFocus.isPresent()){
      //a previous surface had the focus, it now leaves focus
      final WlSurfaceResource wlSurfaceResource = oldFocus.get().getResource().get();
      final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
      if(pointerResource.isPresent()){
        pointerResource.get().leave(display.nextSerial(),
                                    wlSurfaceResource);
      }
    }

    if(newFocus.isPresent()){
      //a new surface has the focus, it now gains focus
      final WlSurfaceResource wlSurfaceResource = newFocus.get().getResource().get();
      final Optional<WlPointerResource> pointerResource = findPointerResource(wlSurfaceResource);
      if(pointerResource.isPresent()){
        pointerResource.get().enter(display.nextSerial(),
                                    wlSurfaceResource,
                                    //TODO calculate relative coordinates
                                    Fixed.create(0),
                                    Fixed.create(0));
        currentFocus = newFocus;
      }
    }
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

package org.trinity.wayland.input.newt;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import org.freedesktop.wayland.util.Fixed;
import org.trinity.wayland.WlJobExecutor;
import org.trinity.wayland.protocol.WlPointer;
import org.trinity.wayland.protocol.WlSeat;

import java.util.Optional;

public class GLWindowSeat implements MouseListener, KeyListener{

  private final WlSeat wlSeat;
  private final WlJobExecutor wlJobExecutor;

  public GLWindowSeat(WlSeat wlSeat,
                      WlJobExecutor wlJobExecutor) {
    this.wlSeat = wlSeat;
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

    this.wlJobExecutor.submit(() -> {
      //TODO find underlying wlsurface (if any)
      //TODO give focus to underlying surface (if different focus surface)

      final Optional<WlPointer> optionalWlPointer = wlSeat.getOptionalWlPointer();
      final WlPointer wlPointer = optionalWlPointer.get();
      wlPointer.getResources().forEach((wlPointerResource) ->
                                               wlPointerResource.motion((int) time,
                                                                        //TODO
                                                                        Fixed.create(0),
                                                                        Fixed.create(0)));
    });
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

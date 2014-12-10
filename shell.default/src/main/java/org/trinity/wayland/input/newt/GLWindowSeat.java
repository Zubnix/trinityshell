package org.trinity.wayland.input.newt;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import org.trinity.wayland.protocol.WlSeat;

public class GLWindowSeat implements MouseListener, KeyListener{

  private final WlSeat wlSeat;

  public GLWindowSeat(WlSeat wlSeat) {
    this.wlSeat = wlSeat;
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

  }

  @Override
  public void mouseDragged(final MouseEvent e) {

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

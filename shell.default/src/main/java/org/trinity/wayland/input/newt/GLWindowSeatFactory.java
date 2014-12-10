package org.trinity.wayland.input.newt;


import com.jogamp.newt.opengl.GLWindow;

import org.trinity.wayland.protocol.WlDataDevice;
import org.trinity.wayland.protocol.WlDataDeviceFactory;
import org.trinity.wayland.protocol.WlKeyboardFactory;
import org.trinity.wayland.protocol.WlPointerFactory;
import org.trinity.wayland.protocol.WlSeat;
import org.trinity.wayland.protocol.WlSeatFactory;
import org.trinity.wayland.protocol.WlTouch;

import java.util.Optional;

import javax.inject.Inject;

public class GLWindowSeatFactory {

  private final WlDataDeviceFactory wlDataDeviceFactory;
  private final WlSeatFactory       wlSeatFactory;
  private final WlKeyboardFactory   wlKeyboardFactory;
  private final WlPointerFactory    wlPointerFactory;

  @Inject
  GLWindowSeatFactory(WlDataDeviceFactory wlDataDeviceFactory,
                      WlSeatFactory       wlSeatFactory,
                      WlKeyboardFactory   wlKeyboardFactory,
                      WlPointerFactory    wlPointerFactory) {
    this.wlDataDeviceFactory = wlDataDeviceFactory;
    this.wlSeatFactory       = wlSeatFactory;
    this.wlKeyboardFactory   = wlKeyboardFactory;
    this.wlPointerFactory    = wlPointerFactory;
  }

  public GLWindowSeat create(final GLWindow glWindow){
    final WlSeat wlSeat = wlSeatFactory.create(wlDataDeviceFactory.create(),
                                               Optional.of(wlPointerFactory.create()),
                                               Optional.of(wlKeyboardFactory.create()),
                                               Optional.<WlTouch>empty());
    GLWindowSeat glWindowSeat = new GLWindowSeat(wlSeat);
    glWindow.addMouseListener(glWindowSeat);
    glWindow.addKeyListener(glWindowSeat);
    return glWindowSeat;
  }
}
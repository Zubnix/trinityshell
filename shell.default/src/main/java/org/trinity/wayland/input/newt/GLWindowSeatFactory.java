package org.trinity.wayland.input.newt;


import com.jogamp.newt.opengl.GLWindow;

import org.trinity.wayland.input.Seat;
import org.trinity.wayland.input.SeatFactory;
import org.trinity.wayland.output.Compositor;
import org.trinity.wayland.protocol.*;

import javax.inject.Inject;
import java.util.Optional;

public class GLWindowSeatFactory {

    private final SeatFactory         wlShellSeatFactory;
    private final WlSeatFactory       wlSeatFactory;
    private final WlDataDeviceFactory wlDataDeviceFactory;
    private final WlPointerFactory    wlPointerFactory;
    private final WlKeyboardFactory   wlKeyboardFactory;

    @Inject
    GLWindowSeatFactory(SeatFactory wlShellSeatFactory,
                        WlSeatFactory wlSeatFactory,
                        WlDataDeviceFactory wlDataDeviceFactory,
                        WlPointerFactory wlPointerFactory,
                        WlKeyboardFactory wlKeyboardFactory) {
      this.wlShellSeatFactory = wlShellSeatFactory;
      this.wlSeatFactory = wlSeatFactory;
      this.wlDataDeviceFactory = wlDataDeviceFactory;
      this.wlPointerFactory = wlPointerFactory;
      this.wlKeyboardFactory = wlKeyboardFactory;
  }

    public GLWindowSeat create(final GLWindow glWindow,
                               final Compositor compositor) {
        final WlSeat wlSeat = this.wlSeatFactory.create(this.wlDataDeviceFactory.create(),
                                                        Optional.of(this.wlPointerFactory.create()),
                                                        Optional.of(this.wlKeyboardFactory.create()),
                                                        Optional.<WlTouch>empty());
        final Seat seat = this.wlShellSeatFactory.create(wlSeat,
                                                         compositor);
        GLWindowSeat glWindowSeat = new GLWindowSeat(seat);
        glWindow.addMouseListener(glWindowSeat);
        glWindow.addKeyListener(glWindowSeat);
        return glWindowSeat;
    }
}
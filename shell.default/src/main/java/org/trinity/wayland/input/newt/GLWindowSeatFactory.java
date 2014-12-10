package org.trinity.wayland.input.newt;


import com.jogamp.newt.opengl.GLWindow;
import org.trinity.wayland.WlJobExecutor;
import org.trinity.wayland.protocol.*;

import javax.inject.Inject;
import java.util.Optional;

public class GLWindowSeatFactory {

    private final WlDataDeviceFactory wlDataDeviceFactory;
    private final WlSeatFactory       wlSeatFactory;
    private final WlKeyboardFactory   wlKeyboardFactory;
    private final WlPointerFactory    wlPointerFactory;
    private final WlJobExecutor wlJobExecutor;

    @Inject
    GLWindowSeatFactory(WlDataDeviceFactory wlDataDeviceFactory,
                        WlSeatFactory wlSeatFactory,
                        WlKeyboardFactory wlKeyboardFactory,
                        WlPointerFactory wlPointerFactory,
                        WlJobExecutor wlJobExecutor) {
        this.wlDataDeviceFactory = wlDataDeviceFactory;
        this.wlSeatFactory = wlSeatFactory;
        this.wlKeyboardFactory = wlKeyboardFactory;
        this.wlPointerFactory = wlPointerFactory;
        this.wlJobExecutor = wlJobExecutor;
    }

    public GLWindowSeat create(final GLWindow glWindow){
    final WlSeat wlSeat = wlSeatFactory.create(wlDataDeviceFactory.create(),
                                               Optional.of(wlPointerFactory.create()),
                                               Optional.of(wlKeyboardFactory.create()),
                                               Optional.<WlTouch>empty());
    GLWindowSeat glWindowSeat = new GLWindowSeat(wlSeat,
                                                 wlJobExecutor);
    glWindow.addMouseListener(glWindowSeat);
    glWindow.addKeyListener(glWindowSeat);
    return glWindowSeat;
  }
}
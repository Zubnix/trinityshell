package org.trinity.wayland.input.newt;


import com.jogamp.newt.opengl.GLWindow;
import org.freedesktop.wayland.server.Display;
import org.trinity.wayland.WlJobExecutor;
import org.trinity.wayland.WlShellCompositor;
import org.trinity.wayland.protocol.*;

import javax.inject.Inject;
import java.util.Optional;

public class GLWindowSeatFactory {

    private final Display             display;
    private final WlDataDeviceFactory wlDataDeviceFactory;
    private final WlSeatFactory       wlSeatFactory;
    private final WlKeyboardFactory   wlKeyboardFactory;
    private final WlPointerFactory    wlPointerFactory;
    private final WlJobExecutor       wlJobExecutor;

    @Inject
    GLWindowSeatFactory(Display display,
                        WlDataDeviceFactory wlDataDeviceFactory,
                        WlSeatFactory wlSeatFactory,
                        WlKeyboardFactory wlKeyboardFactory,
                        WlPointerFactory wlPointerFactory,
                        WlJobExecutor wlJobExecutor) {
        this.display = display;
        this.wlDataDeviceFactory = wlDataDeviceFactory;
        this.wlSeatFactory = wlSeatFactory;
        this.wlKeyboardFactory = wlKeyboardFactory;
        this.wlPointerFactory = wlPointerFactory;
        this.wlJobExecutor = wlJobExecutor;
    }

    public GLWindowSeat create(final GLWindow glWindow,
                               final WlShellCompositor wlShellCompositor) {
        final WlSeat wlSeat = this.wlSeatFactory.create(this.wlDataDeviceFactory.create(),
                                                        Optional.of(this.wlPointerFactory.create()),
                                                        Optional.of(this.wlKeyboardFactory.create()),
                                                        Optional.<WlTouch>empty());
        GLWindowSeat glWindowSeat = new GLWindowSeat(this.display,
                                                     wlSeat,
                                                     wlShellCompositor,
                                                     this.wlJobExecutor);
        glWindow.addMouseListener(glWindowSeat);
        glWindow.addKeyListener(glWindowSeat);
        return glWindowSeat;
    }
}
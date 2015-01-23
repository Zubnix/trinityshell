package org.trinity.wayland.platform.newt;

import com.jogamp.newt.opengl.GLWindow;
import org.trinity.wayland.output.Compositor;
import org.trinity.wayland.output.JobExecutor;
import org.trinity.wayland.output.KeyboardFactory;
import org.trinity.wayland.output.PointerDeviceFactory;
import org.trinity.wayland.protocol.WlKeyboardFactory;
import org.trinity.wayland.protocol.WlPointerFactory;
import org.trinity.wayland.protocol.WlSeat;

import javax.inject.Inject;

public class GLWindowSeatFactory {
    private final JobExecutor       jobExecutor;
    private final WlPointerFactory  wlPointerFactory;
    private final WlKeyboardFactory wlKeyboardFactory;
    private final PointerDeviceFactory pointerDeviceFactory;
    private final KeyboardFactory   keyboardFactory;

    @Inject
    GLWindowSeatFactory(final JobExecutor jobExecutor,
                        final WlPointerFactory wlPointerFactory,
                        final WlKeyboardFactory wlKeyboardFactory,
                        final PointerDeviceFactory pointerDeviceFactory,
                        final KeyboardFactory keyboardFactory) {
        this.jobExecutor = jobExecutor;
        this.wlPointerFactory = wlPointerFactory;
        this.wlKeyboardFactory = wlKeyboardFactory;
        this.pointerDeviceFactory = pointerDeviceFactory;
        this.keyboardFactory = keyboardFactory;
    }

    public GLWindowSeat create(final GLWindow glWindow,
                               final WlSeat wlSeat,
                               final Compositor compositor) {
        //this objects will post input events from the system to our wayland compositor system
        final GLWindowSeat glWindowSeat = new GLWindowSeat(wlSeat,
                                                           this.jobExecutor);
        //FIXME for now we put these here, these should be handled dynamically when a mouse or keyboard is
        //added or removed

        //enable pointer and keyboard for wlseat
        wlSeat.setWlPointer(this.wlPointerFactory.create(this.pointerDeviceFactory.create(compositor)));
        wlSeat.setWlKeyboard(this.wlKeyboardFactory.create(this.keyboardFactory.create()));

        glWindow.addMouseListener(glWindowSeat);
        glWindow.addKeyListener(glWindowSeat);
        return glWindowSeat;
    }
}
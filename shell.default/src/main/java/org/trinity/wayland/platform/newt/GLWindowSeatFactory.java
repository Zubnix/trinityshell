package org.trinity.wayland.platform.newt;


import com.jogamp.newt.opengl.GLWindow;
import org.trinity.wayland.output.Compositor;
import org.trinity.wayland.output.JobExecutor;
import org.trinity.wayland.output.Seat;
import org.trinity.wayland.output.SeatFactory;
import org.trinity.wayland.protocol.*;

import javax.inject.Inject;
import java.util.Optional;

public class GLWindowSeatFactory {

    private final SeatFactory         seatFactory;
    private final WlSeatFactory       wlSeatFactory;
    private final WlDataDeviceFactory wlDataDeviceFactory;
    private final WlPointerFactory    wlPointerFactory;
    private final WlKeyboardFactory   wlKeyboardFactory;
    private final JobExecutor         jobExecutor;

    @Inject
    GLWindowSeatFactory(final SeatFactory seatFactory,
                        final WlSeatFactory wlSeatFactory,
                        final WlDataDeviceFactory wlDataDeviceFactory,
                        final WlPointerFactory wlPointerFactory,
                        final WlKeyboardFactory wlKeyboardFactory,
                        final JobExecutor jobExecutor) {
        this.seatFactory = seatFactory;
        this.wlSeatFactory = wlSeatFactory;
        this.wlDataDeviceFactory = wlDataDeviceFactory;
        this.wlPointerFactory = wlPointerFactory;
        this.wlKeyboardFactory = wlKeyboardFactory;
        this.jobExecutor = jobExecutor;
    }

    public GLWindowSeat create(final GLWindow glWindow,
                               final Compositor compositor) {
        final Seat seat = this.seatFactory.create();
        //these objects will listen for input events
        final WlPointer wlPointer = this.wlPointerFactory.create(compositor);
        final WlKeyboard wlKeyboard = this.wlKeyboardFactory.create();
        seat.getPointer()
            .register(wlPointer);
        seat.getKeyboard()
            .register(wlKeyboard);
        this.wlSeatFactory.create(this.wlDataDeviceFactory.create(),
                                  Optional.of(wlPointer),
                                  Optional.of(wlKeyboard),
                                  Optional.<WlTouch>empty(),
                                  seat);
        //these objects will post input events
        final GLWindowSeat glWindowSeat = new GLWindowSeat(seat,
                                                           this.jobExecutor);
        glWindow.addMouseListener(glWindowSeat);
        glWindow.addKeyListener(glWindowSeat);
        return glWindowSeat;
    }
}
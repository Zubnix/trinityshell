package org.trinity.foundation.display.x11.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.display.api.DisplaySurface;
import org.trinity.display.api.Seat;

import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcb.xcb_flush;
import static org.freedesktop.xcb.LibXcb.xcb_set_input_focus;
import static org.freedesktop.xcb.xcb_input_focus_t.XCB_INPUT_FOCUS_NONE;

/**
 *
 */
public class XSeat implements Seat {

    private static final Logger LOG = LoggerFactory.getLogger(XSeat.class);

    private final XTime xTime;
    private final XEventChannel xEventChannel;

    @Inject
    XSeat(final XTime xTime, final XEventChannel xEventChannel) {
        this.xTime = xTime;
        this.xEventChannel = xEventChannel;
    }

    @Override
    public void giveInputFocus(final DisplaySurface displaySurface) {
        final Integer winId = (Integer) displaySurface.getDisplaySurfaceHandle().getNativeHandle();
        final int time = this.xTime.getTime();
        LOG.debug("[winId={}] set input focus.",
                winId);
        xcb_set_input_focus(this.xEventChannel.getXcbConnection(),
                (short) XCB_INPUT_FOCUS_NONE,
                winId,
                time);
        xcb_flush(this.xEventChannel.getXcbConnection());
    }
}

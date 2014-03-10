package org.trinity.foundation.display.x11.impl;

import org.freedesktop.xcb.xcb_generic_event_t;

import javax.annotation.Nonnull;

/**
 *
 */
public interface XEventHandler {
    void handle(@Nonnull xcb_generic_event_t event_t);

    Integer getEventCode();
}

package org.trinity.x11.defaul;

import org.freedesktop.xcb.xcb_generic_event_t;

import javax.annotation.Nonnull;

/**
 *
 */
public interface XEventHandler {
    void handle(@Nonnull xcb_generic_event_t event);

    Integer getEventCode();
}

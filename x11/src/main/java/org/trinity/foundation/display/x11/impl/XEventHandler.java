package org.trinity.foundation.display.x11.impl;

import com.google.common.base.Optional;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.display.api.DisplaySurface;
import org.trinity.display.api.event.DisplayEvent;

import javax.annotation.Nonnull;

/**
 *
 */
public interface XEventHandler {
    Optional<? extends DisplayEvent> handle(@Nonnull xcb_generic_event_t event_t);

    Optional<DisplaySurface> getTarget(@Nonnull xcb_generic_event_t event_t);

    Integer getEventCode();
}

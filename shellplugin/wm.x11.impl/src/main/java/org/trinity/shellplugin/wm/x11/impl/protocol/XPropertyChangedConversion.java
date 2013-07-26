package org.trinity.shellplugin.wm.x11.impl.protocol;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.display.x11.api.XEventConversion;
import org.trinity.foundation.display.x11.api.XWindowCache;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import static org.freedesktop.xcb.LibXcbConstants.XCB_PROPERTY_NOTIFY;

@Bind(multiple = true)
@Singleton
public class XPropertyChangedConversion implements XEventConversion {

    private static final Integer EVENT_CODE = XCB_PROPERTY_NOTIFY;

    private final XWindowCache xWindowCache;

    @Inject
    XPropertyChangedConversion(final XWindowCache xWindowCache) {
        this.xWindowCache = xWindowCache;
    }

    @Override
    public DisplayEvent convert(@Nonnull final xcb_generic_event_t event) {
        final xcb_property_notify_event_t property_notify_event = new xcb_property_notify_event_t(xcb_generic_event_t.getCPtr(event),
                false);
        final int clientId = property_notify_event.getWindow();
        xWindowCache.getWindow(clientId).post(property_notify_event);

        //no conversion possible
        return null;
    }

    @Override
    public AsyncListenable getTarget(@Nonnull final xcb_generic_event_t event) {
        //no conversion so no target needed
        return null;
    }

    @Override
    public Integer getEventCode() {
        return EVENT_CODE;
    }
}

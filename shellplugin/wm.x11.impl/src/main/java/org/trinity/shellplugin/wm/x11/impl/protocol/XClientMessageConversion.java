package org.trinity.shellplugin.wm.x11.impl.protocol;

import com.google.inject.Singleton;
import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_client_message_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.AsyncListenable;
import org.trinity.foundation.display.x11.api.XEventConversion;
import org.trinity.foundation.display.x11.api.XWindowCache;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_CLIENT_MESSAGE;

@Bind(multiple = true)
@Singleton
public class XClientMessageConversion implements XEventConversion {

    private static final Integer EVENT_CODE = XCB_CLIENT_MESSAGE;

    private final XWindowCache xWindowCache;

    @Inject
    XClientMessageConversion(final XWindowCache xWindowCache) {
        this.xWindowCache = xWindowCache;
    }

    @Override
    public DisplayEvent convert(@Nonnull final xcb_generic_event_t event) {
        final xcb_client_message_event_t client_message_event_t = new xcb_client_message_event_t(xcb_generic_event_t.getCPtr(event),
                false);
        xWindowCache.getWindow(client_message_event_t.getWindow()).post(client_message_event_t);
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

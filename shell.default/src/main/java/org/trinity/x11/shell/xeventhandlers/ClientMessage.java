package org.trinity.x11.shell.xeventhandlers;

import org.freedesktop.xcb.xcb_client_message_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.trinity.x11.XEventHandler;

import javax.annotation.concurrent.Immutable;
import javax.annotation.Nonnull;
import javax.inject.Inject;

import static org.freedesktop.xcb.LibXcbConstants.XCB_CLIENT_MESSAGE;

/**
 * Created by Erik De Rijcke on 6/3/14.
 */

@Immutable
public class ClientMessage implements XEventHandler {

    private static final Integer EVENT_CODE = XCB_CLIENT_MESSAGE;

    @Inject
    ClientMessage() {
    }

    @Override
    public void handle(@Nonnull final xcb_generic_event_t event) {
        final xcb_client_message_event_t client_message = cast(event);
        final int type = client_message.getType();
        //TODO interpret type
    }

    @Override
    public Integer getEventCode() {
        return EVENT_CODE;
    }

    private xcb_client_message_event_t cast(final xcb_generic_event_t event) {
        return new xcb_client_message_event_t(xcb_generic_event_t.getCPtr(event),
                                              false);
    }
}

package org.trinity.foundation.display.x11.impl;


import com.google.common.base.Optional;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Test;
import org.trinity.foundation.api.display.event.DisplayEvent;
import org.trinity.foundation.api.shared.Listenable;

import java.util.HashSet;

import static org.mockito.Mockito.*;

public class TestXEventHandlers {

    @Test
    public void testEventDelegationHappy() {
        //given
        //event handler with eventcode 0
        //event handler with eventcode 1
        final xcb_generic_event_t xcb_generic_event_0 = mock(xcb_generic_event_t.class);
        final xcb_generic_event_t xcb_generic_event_1 = mock(xcb_generic_event_t.class);

        when(xcb_generic_event_0.getResponse_type()).thenReturn((short) 1);
        when(xcb_generic_event_1.getResponse_type()).thenReturn((short) 3);

        final XEventHandler xEventHandler0 = mock(XEventHandler.class);
        final XEventHandler xEventHandler1 = mock(XEventHandler.class);
        final Listenable target = mock(Listenable.class);
        final DisplayEvent displayEvent = mock(DisplayEvent.class);

        when(xEventHandler0.getEventCode()).thenReturn(0);
        when(xEventHandler1.getEventCode()).thenReturn(1);
        when(xEventHandler1.getTarget(xcb_generic_event_0)).thenReturn((Optional) Optional.of(target));
        when(xEventHandler1.handle(xcb_generic_event_0)).thenReturn((Optional) Optional.of(displayEvent));

        final XEventChannel eventBus = mock(XEventChannel.class);

        final XEventHandlers xEventHandlers = new XEventHandlers(new HashSet() {{add(xEventHandler0); add(xEventHandler1);}},
                                                           eventBus);
        //when
        //an event arrives with code 1
        //an event arrives with code 3
        xEventHandlers.handleXEvent(xcb_generic_event_0);
        xEventHandlers.handleXEvent(xcb_generic_event_1);

        //then
        //event handler with eventcode 1 should handle event with code 1
        //display event is posted on target
        verify(xEventHandler1).handle(xcb_generic_event_0);
        verify(xEventHandler0,
               never()).handle((xcb_generic_event_t) any());

        verify(target).post(displayEvent);
    }
}

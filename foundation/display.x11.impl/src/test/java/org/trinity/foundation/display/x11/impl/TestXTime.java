package org.trinity.foundation.display.x11.impl;


import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.xcb_button_press_event_t;
import org.freedesktop.xcb.xcb_enter_notify_event_t;
import org.freedesktop.xcb.xcb_key_press_event_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestXTime {

    @Test
    public void testTimeUpdates() {
        //given
        //a button pressed event with its own X time
        //a key pressed event with its own X time
        //a property notify event with its own X time
        //an enter notify event with its own X time
        //an xtime object
        final EventBus xEventBus = mock(EventBus.class);
        final XTime xTime = new XTime(xEventBus);

        final xcb_button_press_event_t button_press_event = mock(xcb_button_press_event_t.class);
        final xcb_key_press_event_t key_press_event = mock(xcb_key_press_event_t.class);
        final xcb_property_notify_event_t property_notify_event = mock(xcb_property_notify_event_t.class);
        final xcb_enter_notify_event_t enter_notify_event = mock(xcb_enter_notify_event_t.class);

        when(button_press_event.getTime()).thenReturn(0);
        when(key_press_event.getTime()).thenReturn(1);
        when(property_notify_event.getTime()).thenReturn(2);
        when(enter_notify_event.getTime()).thenReturn(3);
        //when
        //a button pressed event arrives
        //a key pressed event arrives
        //a property notify event arrives
        //an enter notify event arrives
        xTime.handleButtonPressed(button_press_event);
        xTime.handleKeyPressed(key_press_event);
        xTime.handlePropertyNotify(property_notify_event);
        xTime.handleEnterNotify(enter_notify_event);

        //then
        //the time is correctly updated
        assertEquals(3,
                     xTime.getTime());
    }
}

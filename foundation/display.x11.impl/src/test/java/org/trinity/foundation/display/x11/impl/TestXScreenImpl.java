package org.trinity.foundation.display.x11.impl;

import org.freedesktop.xcb.xcb_screen_t;
import org.junit.Test;
import org.trinity.foundation.api.shared.Size;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TestXScreenImpl {

    @Test
    public void testScreenSizeHappy() {
        //given
        //an X screen with a given width and height in pixels
        final xcb_screen_t xcb_screen = mock(xcb_screen_t.class);

        final int width = 100;
        final int height = 100;
        when(xcb_screen.getWidth_in_pixels()).thenReturn(width);
        when(xcb_screen.getHeight_in_pixels()).thenReturn(height);

        //when
        //the screen pixel size is requested
        final XScreenImpl xScreen = new XScreenImpl(xcb_screen);
        final Size size = xScreen.getSize();

        //then
        //the correct pixel size is reported
        verify(xcb_screen).getWidth_in_pixels();
        verify(xcb_screen).getHeight_in_pixels();

        assertEquals(width,
                     size.getWidth());
        assertEquals(height,
                     size.getHeight());
    }
}

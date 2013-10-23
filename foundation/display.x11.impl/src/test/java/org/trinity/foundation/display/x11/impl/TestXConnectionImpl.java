package org.trinity.foundation.display.x11.impl;


import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.ByteBuffer;

import static org.freedesktop.xcb.LibXcb.xcb_connect;
import static org.freedesktop.xcb.LibXcb.xcb_disconnect;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({XConnectionImpl.class,
                 LibXcb.class})
public class TestXConnectionImpl {

    @Test
    public void testOpen() {

        //GIVEN: DISPLAY=":0"
        String display = ":0";
        SWIGTYPE_p_xcb_connection_t connection = mock(SWIGTYPE_p_xcb_connection_t.class);

        mockStatic(LibXcb.class);
        when(xcb_connect(eq(display),
                         Matchers.<ByteBuffer>any())).thenReturn(connection);

        mockStatic(System.class);
        when(System.getenv(eq("DISPLAY"))).thenReturn(display);

        //WHEN x connection object is constructed
        new XConnectionImpl();

        //THEN a new xcb X connection should be established on ":0".
        verifyStatic();
        System.getenv("DISPLAY");

        verifyStatic();
        xcb_connect(eq(display),
                    Matchers.<ByteBuffer>any());
    }

    @Test
    public void testClose() {
        //GIVEN: open display on ":0"
        String display = ":0";
        SWIGTYPE_p_xcb_connection_t connection = mock(SWIGTYPE_p_xcb_connection_t.class);

        mockStatic(LibXcb.class);
        when(xcb_connect(eq(display),
                         Matchers.<ByteBuffer>any())).thenReturn(connection);

        mockStatic(System.class);
        when(System.getenv(eq("DISPLAY"))).thenReturn(display);

        XConnectionImpl xConnection = new XConnectionImpl();

        //WHEN x connection object is close
        xConnection.close();

        //THEN the underlying connection to the X server should be closed
        verifyStatic();
        xcb_disconnect(connection);
    }
}

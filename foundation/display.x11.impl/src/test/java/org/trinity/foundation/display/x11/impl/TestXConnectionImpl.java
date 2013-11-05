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
        //given
        //an X display server on :0
        final String display = ":0";
        final SWIGTYPE_p_xcb_connection_t connection = mock(SWIGTYPE_p_xcb_connection_t.class);

        mockStatic(LibXcb.class);
        when(xcb_connect(eq(display),
                         Matchers.<ByteBuffer>any())).thenReturn(connection);

        mockStatic(System.class);
        when(System.getenv(eq("DISPLAY"))).thenReturn(display);

        //when
        //XConnectionImpl is constructed
        new XConnectionImpl();

        //then
        //a new xcb X connection should be established on ":0".
        verifyStatic();
        System.getenv("DISPLAY");

        verifyStatic();
        xcb_connect(eq(display),
                    Matchers.<ByteBuffer>any());
    }

    @Test
    public void testClose() {
        //given
        //an open XConnectionImpl on ":0"
        final String display = ":0";
        final SWIGTYPE_p_xcb_connection_t connection = mock(SWIGTYPE_p_xcb_connection_t.class);

        mockStatic(LibXcb.class);
        when(xcb_connect(eq(display),
                         Matchers.<ByteBuffer>any())).thenReturn(connection);

        mockStatic(System.class);
        when(System.getenv(eq("DISPLAY"))).thenReturn(display);

        final XConnectionImpl xConnection = new XConnectionImpl();

        //when
        //XConnectionImpl object is close
        xConnection.close();

        //then
        //the underlying connection to the X server should be closed
        verifyStatic();
        xcb_disconnect(connection);
    }
}

package org.trinity.foundation.display.x11.impl;


import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;

import static org.freedesktop.xcb.LibXcb.xcb_connect;
import static org.freedesktop.xcb.LibXcb.xcb_disconnect;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({XEventChannelImpl.class,
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
        //XEventChannelImpl is constructed
        new XEventChannelImpl();

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
        //an open XEventChannelImpl on ":0"
        final String display = ":0";
        final SWIGTYPE_p_xcb_connection_t connection = mock(SWIGTYPE_p_xcb_connection_t.class);

        mockStatic(LibXcb.class);
        when(xcb_connect(eq(display),
                         Matchers.<ByteBuffer>any())).thenReturn(connection);

        mockStatic(System.class);
        when(System.getenv(eq("DISPLAY"))).thenReturn(display);

        final XEventChannelImpl xConnection = new XEventChannelImpl();

        //when
        //XEventChannelImpl object is close
        xConnection.close();

        //then
        //the underlying connection to the X server should be closed
        verifyStatic();
        xcb_disconnect(connection);
    }

    @Test
    public void testEventFetchHappy() {
        //TODO
//        //given
//        //an X server with no errors
//        mockStatic(LibXcb.class);
//
//        final xcb_generic_event_t xcb_generic_event = mock(xcb_generic_event_t.class);
//
//        when(xcb_connection_has_error(this.xcb_connection)).thenReturn(0);
//
//        when(xcb_wait_for_event(this.xcb_connection)).thenReturn(xcb_generic_event);
//
//        //when
//        //an event arrives
//        new XEventPump(this.xConnection).call();
//
//        //then
//        //the event is passed on to the x event bus
//        verify(this.xEventBus).post(xcb_generic_event);
    }

    @Test(expected = Throwable.class)
    public void testEventFetchXError() {
        //TODO
//        //given
//        //an X server with errors
//        mockStatic(LibXcb.class);
//        when(xcb_connection_has_error(this.xcb_connection)).thenReturn(1);
//
//        //when
//        //the event pump tries to wait for an event
//        new XEventPump(this.xConnection).call();
//        //then
//        //an error is thrown
    }

    @Test
    public void testEventFetchStartStopHappy() throws InterruptedException, TimeoutException {
        //TODO
//        //given
//        //an X server with no errors
//        mockStatic(LibXcb.class);
//
//        final xcb_generic_event_t xcb_generic_event = mock(xcb_generic_event_t.class);
//
//        when(xcb_connection_has_error(this.xcb_connection)).thenReturn(0);
//
//        when(xcb_wait_for_event(this.xcb_connection)).thenReturn(xcb_generic_event);
//
//        //when
//        //the x event pump is stopped
//        //an event arrives
//        final XEventPump xEventPump = new XEventPump(this.xConnection);
//        xEventPump.stop();
//
//        final CountDownLatch waitForEvent = new CountDownLatch(1);
//        //build a new thread else we might block indefinitely.
//        new Thread() {
//            @Override
//            public void run() {
//                xEventPump.call();
//                waitForEvent.countDown();
//            }
//        }.start();
//
//        //then
//        //the event is passed on to the x event bus after the x event pump is started
//        verify(this.xEventBus,
//                never()).post(xcb_generic_event);
//        xEventPump.start();
//        final boolean timeout = !waitForEvent.await(1,
//                TimeUnit.SECONDS);
//        if(timeout) {
//            throw new TimeoutException("Timeout while waiting 1 second for X event to arrive.");
//        } else {
//            verify(this.xEventBus).post(xcb_generic_event);
//        }
    }

    @Test(timeout = 5000,
            expected = Throwable.class)
    public void testEventFetchStartStopXError() {
        //TODO
//        //given
//        //an X server with errors
//        mockStatic(LibXcb.class);
//        when(xcb_connection_has_error(this.xcb_connection)).thenReturn(1);
//
//        //when
//        //the x event pump is stopped
//        //an X server with errors
//        //the x event pump is started
//        final XEventPump xEventPump = new XEventPump(this.xConnection);
//        xEventPump.stop();
//        xEventPump.call();
//
//        //then
//        //an error is thrown
    }
}

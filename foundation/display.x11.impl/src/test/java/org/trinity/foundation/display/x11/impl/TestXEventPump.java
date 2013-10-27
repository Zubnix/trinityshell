package org.trinity.foundation.display.x11.impl;


import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.display.x11.api.XConnection;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_wait_for_event;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LibXcb.class)
public class TestXEventPump {

    @Mock
    private EventBus xEventBus;
    @Mock
    private ListeningExecutorService xExecutor;
    @Mock
    private XConnection xConnection;
    @Mock
    private ExecutorService xEventPumpExecutor;
    @Mock
    private SWIGTYPE_p_xcb_connection_t xcb_connection;

    @Before
    public void setup() {
        when(xExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                Object arg0 = invocation.getArguments()[0];
                Callable<?> submittedCallable = (Callable<?>) arg0;
                Object result = submittedCallable.call();
                return Futures.immediateFuture(result);
            }
        });


        when(xConnection.getConnectionReference()).thenReturn(xcb_connection);
    }

    @Test
    public void testEventFetchHappy() {
        //given
        //an X server with no errors
        mockStatic(LibXcb.class);

        xcb_generic_event_t xcb_generic_event = mock(xcb_generic_event_t.class);

        when(xcb_connection_has_error(xcb_connection)).thenReturn(0);

        when(xcb_wait_for_event(xcb_connection)).thenReturn(xcb_generic_event);

        //when
        //an event arrives
        new XEventPump(xConnection,
                       xEventBus,
                       xExecutor,
                       xEventPumpExecutor).call();

        //then
        //the event is passed on to the x event bus
        verify(xEventBus).post(xcb_generic_event);
    }

    @Test(expected = Throwable.class)
    public void testEventFetchXError() {
        //given
        //an X server with errors
        mockStatic(LibXcb.class);
        when(xcb_connection_has_error(xcb_connection)).thenReturn(1);

        //when
        //the event pump tries to wait for an event
        new XEventPump(xConnection,
                       xEventBus,
                       xExecutor,
                       xEventPumpExecutor).call();
        //then
        //an error is thrown
    }

    @Test
    public void testEventFetchStartStopHappy() throws InterruptedException, TimeoutException {
        //given
        //an X server with no errors
        mockStatic(LibXcb.class);

        xcb_generic_event_t xcb_generic_event = mock(xcb_generic_event_t.class);

        when(xcb_connection_has_error(xcb_connection)).thenReturn(0);

        when(xcb_wait_for_event(xcb_connection)).thenReturn(xcb_generic_event);

        //when
        //the x event pump is stopped
        //an event arrives
        final XEventPump xEventPump = new XEventPump(xConnection,
                                                     xEventBus,
                                                     xExecutor,
                                                     xEventPumpExecutor);
        xEventPump.stop();

        final CountDownLatch waitForEvent = new CountDownLatch(1);
        //create a new thread else we might block indefinitely.
        new Thread() {
            @Override
            public void run() {
                xEventPump.call();
                waitForEvent.countDown();
            }
        }.start();

        //then
        //the event is passed on to the x event bus after the x event pump is started
        verify(xEventBus,
               never()).post(xcb_generic_event);
        xEventPump.start();
        boolean timeout = !waitForEvent.await(1,
                                              TimeUnit.SECONDS);
        if(timeout) {
            throw new TimeoutException("Timeout while waiting 1 second for X event to arrive.");
        } else {
            verify(xEventBus).post(xcb_generic_event);
        }
    }

    @Test(timeout = 5000,
          expected = Throwable.class)
    public void testEventFetchStartStopXError() {
        //given
        //an X server with errors
        mockStatic(LibXcb.class);
        when(xcb_connection_has_error(xcb_connection)).thenReturn(1);

        //when
        //the x event pump is stopped
        //an X server with errors
        //the x event pump is started
        final XEventPump xEventPump = new XEventPump(xConnection,
                                                     xEventBus,
                                                     xExecutor,
                                                     xEventPumpExecutor);
        xEventPump.stop();
        xEventPump.call();

        //then
        //an error is thrown
    }
}

package org.trinity.x11.xevents;

import org.freedesktop.xcb.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.x11.XEventLoop;
import org.trinity.x11.XSurfacePool;
import org.trinity.x11.shell.xeventhandlers.MapRequest;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class,
        LibXcbJNI.class})
public class TestMapRequestHandler {
    @Mock
    private XEventLoop   xEventLoop;
    private XSurfacePool xSurfacePool;
    @InjectMocks
    private MapRequest   mapRequest;

    @Mock
    private xcb_generic_event_t         xcb_generic_event;
    @Mock
    private SWIGTYPE_p_xcb_connection_t xcb_connection;

    @Before
    public void setup() {
        when(this.xEventLoop.getXcbConnection()).thenReturn(this.xcb_connection);
    }

    @Test
    public void testEventHandling() {
        //given
        //a MapRequest
        //an xcb_generic_event_t
        mockStatic(LibXcbJNI.class);
        mockStatic(LibXcb.class);

        //when
        //an xcb_generic_event_t event arrives
        this.mapRequest.handle(this.xcb_generic_event);

        //then
        //the xcb_map_request_event_t is posted on the x event bus
        //the event is converted to a ShowRequest
        verify(this.xEventLoop).post(isA(xcb_map_request_event_t.class));
    }
}
package org.trinity.x11.xevents;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.x11.XEventLoop;
import org.trinity.x11.XSurfacePool;
import org.trinity.x11.shell.xeventhandlers.ConfigureRequest;

import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class,
        LibXcbJNI.class})
public class TestConfigureRequestHandler {

    @Mock
    private XEventLoop       xEventLoop;
    @Mock
    private XSurfacePool     xSurfacePool;
    @InjectMocks
    private ConfigureRequest configureRequest;

    @Mock
    private xcb_generic_event_t xcb_generic_event;

    @Test
    public void testEventHandling() {
        //given
        //a ConfigureRequest
        //an xcb_generic_event_t

        //when
        //an xcb_generic_event_t event arrives
        this.configureRequest.handle(this.xcb_generic_event);

        //then
        //the xcb_configure_notify_event_t is posted on the x event bus
        //the event is converted to a GeometryRequest

        verify(this.xEventLoop).post(isA(xcb_configure_request_event_t.class));
    }
}

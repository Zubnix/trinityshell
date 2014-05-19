package org.trinity.x11.defaul.xevents;


import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.x11.defaul.XEventLoop;
import org.trinity.x11.defaul.shell.xeventhandlers.GenericError;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LibXcbJNI.class)
public class TestGenericErrorHandler {
    @Mock
    private XEventLoop          xEventLoop;
    @InjectMocks
    private GenericError        genericError;
    @Mock
    private xcb_generic_event_t xcb_generic_event;

    @Test
    public void testEventHandling() {
        //given
        //a GenericError
        //an xcb_generic_event_t
        mockStatic(LibXcbJNI.class);

        //when
        //an xcb_generic_event_t event arrives
        this.genericError.handle(this.xcb_generic_event);

        //then
        //the xcb_generic_error_t is posted on the x event bus
        //the event is not converted to a DisplayEvent
        verify(this.xEventLoop).post(isA(xcb_generic_error_t.class));
    }
}

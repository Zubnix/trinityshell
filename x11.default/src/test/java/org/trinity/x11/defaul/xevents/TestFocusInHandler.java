package org.trinity.x11.defaul.xevents;

import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.xcb_focus_in_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.x11.defaul.XEventLoop;
import org.trinity.x11.defaul.XSurfacePool;

import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LibXcbJNI.class)
public class TestFocusInHandler {
    @Mock
    private XEventLoop          xEventLoop;
    @Mock
    private XSurfacePool        xSurfacePool;
    @InjectMocks
    private FocusIn             focusIn;
    @Mock
    private xcb_generic_event_t xcb_generic_event;

    private final int targetWindowId = 123;

    @Test
    public void testEventHandling() {
        //given
        //a FocusIn
        //an xcb_generic_event_t

        //when
        //an xcb_generic_event_t event arrives
        this.focusIn.handle(this.xcb_generic_event);

        //then
        //the xcb_focus_in_event_t is posted on the x event bus
        //the event is converted to a DestroyNotify
        verify(this.xEventLoop).post(isA(xcb_focus_in_event_t.class));
    }
}

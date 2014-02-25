package org.trinity.foundation.display.x11.impl.event;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_map_request_event_t;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.api.display.Display;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.api.display.event.ShowRequest;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePoolImpl;

import java.nio.ByteBuffer;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcbJNI.xcb_map_request_event_t_window_get;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_ENTER_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_LEAVE_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_STRUCTURE_NOTIFY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class,
        LibXcbJNI.class})
public class TestMapRequestHandler {
    @Mock
    private EventBus xEventBus;
    @Mock
    private XConnection xConnection;
    @Mock
    private DisplaySurfacePoolImpl xWindowPool;
    @Mock
    private Display display;
    @InjectMocks
    private MapRequestHandler mapRequestHandler;

    @Mock
    private xcb_generic_event_t xcb_generic_event;
    @Mock
    private SWIGTYPE_p_xcb_connection_t xcb_connection;

    private final int targetWindowId = 123;

    @Before
    public void setup() {
        when(xConnection.getConnectionReference()).thenReturn(xcb_connection);
    }

    @Test
    public void testEventHandling() {
        //given
        //a MapRequestHandler
        //an xcb_generic_event_t
        mockStatic(LibXcbJNI.class);
        mockStatic(LibXcb.class);

        //when
        //an xcb_generic_event_t event arrives
        final Optional<ShowRequest> showRequestOptional = mapRequestHandler.handle(xcb_generic_event);

        //then
        //the xcb_map_request_event_t is posted on the x event bus
        //the event is converted to a ShowRequest
        verify(xEventBus).post(isA(xcb_map_request_event_t.class));
        assertTrue(showRequestOptional.isPresent());
    }

    @Test
    public void testGetTargetClientKnown() {
        //given
        //a MapRequestHandler
        //an xcb_generic_event_t from an known client
        mockStatic(LibXcbJNI.class);
        when(xcb_map_request_event_t_window_get(anyLong(),
                (xcb_map_request_event_t) any()))
                .thenReturn(this.targetWindowId);
        when(this.xWindowPool.isPresent((DisplaySurfaceHandle) any())).thenReturn(TRUE);

        final DisplaySurface displaySurface = mock(DisplaySurface.class);
        when(displaySurface.getDisplaySurfaceHandle()).thenReturn(XWindowHandle.create(this.targetWindowId));
        when(this.xWindowPool.getDisplaySurface((DisplaySurfaceHandle) any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object arg0 = invocation.getArguments()[0];
                final XWindowHandle xWindowHandle = (XWindowHandle) arg0;
                if (xWindowHandle != null && xWindowHandle.getNativeHandle().equals(targetWindowId)) {
                    return displaySurface;
                }
                return null;
            }
        });

        //when
        //the target of the xcb_generic_event_t event is requested
        final Optional<DisplaySurface> target = mapRequestHandler.getTarget(xcb_generic_event);

        //then
        //the correct DisplaySurface is returned
        //no display surface creation notify is fired
        final ArgumentCaptor<XWindowHandle> windowHandleArgumentCaptor = ArgumentCaptor.forClass(XWindowHandle.class);
        verify(this.xWindowPool).getDisplaySurface(windowHandleArgumentCaptor.capture());
        assertEquals((Integer) this.targetWindowId,
                windowHandleArgumentCaptor.getValue().getNativeHandle());
        assertTrue(target.isPresent());
        verify(this.display,
                never()).post(isA(DisplaySurfaceCreationNotify.class));
    }

    @Test
    public void testGetTargetClientUnknown() {
        //given
        //a MapRequestHandler
        //an xcb_generic_event_t from an unknown client
        mockStatic(LibXcbJNI.class);
        mockStatic(LibXcb.class);

        when(xcb_map_request_event_t_window_get(anyLong(),
                (xcb_map_request_event_t) any()))
                .thenReturn(this.targetWindowId);
        when(this.xWindowPool.isPresent((DisplaySurfaceHandle) any())).thenReturn(FALSE);

        final DisplaySurface displaySurface = mock(DisplaySurface.class);
        when(displaySurface.getDisplaySurfaceHandle()).thenReturn(XWindowHandle.create(this.targetWindowId));
        when(this.xWindowPool.getDisplaySurface((DisplaySurfaceHandle) any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object arg0 = invocation.getArguments()[0];
                final XWindowHandle xWindowHandle = (XWindowHandle) arg0;
                if(xWindowHandle != null && xWindowHandle.getNativeHandle().equals(targetWindowId)) {
                    return displaySurface;
                }
                return null;
            }
        });

        //when
        //the target of the xcb_generic_event_t event is requested
        final Optional<DisplaySurface> target = mapRequestHandler.getTarget(xcb_generic_event);

        //then
        //the correct DisplaySurface is returned
        //the client's event signaling is configured
        //a client creation notify is fired on the display object
        assertTrue(target.isPresent());

        final ArgumentCaptor<ByteBuffer> argumentCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        PowerMockito.verifyStatic();
        xcb_change_window_attributes(eq(this.xcb_connection),
                eq(this.targetWindowId),
                eq(XCB_CW_EVENT_MASK),
                argumentCaptor.capture());
        final ByteBuffer value = argumentCaptor.getValue();
        value.rewind();
        assertEquals(XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW
                | XCB_EVENT_MASK_STRUCTURE_NOTIFY,
                value.getInt());

        verify(this.display).post(isA(DisplaySurfaceCreationNotify.class));
    }
}
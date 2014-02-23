package org.trinity.foundation.display.x11.impl.event;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
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
import org.trinity.foundation.api.display.event.GeometryRequest;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePoolImpl;

import java.nio.ByteBuffer;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcbJNI.*;
import static org.freedesktop.xcb.xcb_config_window_t.*;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class,
                 LibXcbJNI.class})
public class TestConfigureRequestHandler {

    @Mock
    private EventBus xEventBus;
    @Mock
    private XConnection xConnection;
    @Mock
    private DisplaySurfacePoolImpl xWindowPool;
    @Mock
    private Display display;
    @InjectMocks
    private ConfigureRequestHandler configureRequestHandler;

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
        //a ConfigureRequestHandler
        //an xcb_generic_event_t
        mockStatic(LibXcbJNI.class);
        mockStatic(LibXcb.class);

        final short x = 5;
        final short y = 10;
        final int width = 15;
        final int height = 30;
        final int border = 3;

        final int valueMask = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y | XCB_CONFIG_WINDOW_WIDTH;

        when(xcb_configure_request_event_t_x_get(anyLong(),
                                                 (xcb_configure_request_event_t) any())).thenReturn(x);
        when(xcb_configure_request_event_t_y_get(anyLong(),
                                                 (xcb_configure_request_event_t) any())).thenReturn(y);
        when(xcb_configure_request_event_t_width_get(anyLong(),
                                                     (xcb_configure_request_event_t) any())).thenReturn(width);
        when(xcb_configure_request_event_t_height_get(anyLong(),
                                                      (xcb_configure_request_event_t) any())).thenReturn(height);
        when(xcb_configure_request_event_t_border_width_get(anyLong(),
                                                            (xcb_configure_request_event_t) any())).thenReturn(border);
        when(xcb_configure_request_event_t_value_mask_get(anyLong(),
                                                          (xcb_configure_request_event_t) any())).thenReturn(valueMask);

        //when
        //an xcb_generic_event_t event arrives
        final Optional<GeometryRequest> geometryRequestOptional = this.configureRequestHandler.handle(this.xcb_generic_event);

        //then
        //the xcb_configure_notify_event_t is posted on the x event bus
        //the event is converted to a GeometryRequest

        verify(xEventBus).post(isA(xcb_configure_request_event_t.class));
        assertTrue(geometryRequestOptional.isPresent());
        assertEquals(x,
                     geometryRequestOptional.get().getGeometry().getPosition().getX());
        assertEquals(y,
                     geometryRequestOptional.get().getGeometry().getPosition().getY());
        assertEquals(width + 2 * border,
                     geometryRequestOptional.get().getGeometry().getSize().getWidth());
        assertEquals(height + 2 * border,
                     geometryRequestOptional.get().getGeometry().getSize().getHeight());
        assertTrue(geometryRequestOptional.get().configureX());
        assertTrue(geometryRequestOptional.get().configureY());
        assertTrue(geometryRequestOptional.get().configureWidth());
        assertFalse(geometryRequestOptional.get().configureHeight());
    }

    @Test
    public void testGetTargetClientKnown() {
        //given
        //a ConfigureRequestHandler
        //an xcb_generic_event_t  from an known client
        mockStatic(LibXcbJNI.class);
        when(xcb_configure_request_event_t_window_get(anyLong(),
                                                      (xcb_configure_request_event_t) any()))
                .thenReturn(this.targetWindowId);
        when(this.xWindowPool.isPresent((DisplaySurfaceHandle) any())).thenReturn(TRUE);

        final DisplaySurface displaySurface = mock(DisplaySurface.class);
        when(displaySurface.getDisplaySurfaceHandle()).thenReturn(new XWindowHandle(this.targetWindowId));
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
        final Optional<DisplaySurface> target = this.configureRequestHandler.getTarget(this.xcb_generic_event);

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
        //a ConfigureRequestHandler
        //an xcb_generic_event_t from an unknown client
        mockStatic(LibXcbJNI.class);
        mockStatic(LibXcb.class);
        when(xcb_configure_request_event_t_window_get(anyLong(),
                                                      (xcb_configure_request_event_t) any()))
                .thenReturn(this.targetWindowId);
        when(this.xWindowPool.isPresent((DisplaySurfaceHandle) any())).thenReturn(FALSE);

        final DisplaySurface displaySurface = mock(DisplaySurface.class);
        when(displaySurface.getDisplaySurfaceHandle()).thenReturn(new XWindowHandle(this.targetWindowId));
        when(this.xWindowPool.getDisplaySurface((DisplaySurfaceHandle) any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object arg0 = invocation.getArguments()[0];
                final XWindowHandle xWindowHandle = (XWindowHandle) arg0;
                if(xWindowHandle != null && xWindowHandle.getNativeHandle().equals(TestConfigureRequestHandler.this.targetWindowId)) {
                    return displaySurface;
                }
                return null;
            }
        });

        //when
        //the target of the xcb_generic_event_t event is requested
        final Optional<DisplaySurface> target = this.configureRequestHandler.getTarget(this.xcb_generic_event);

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

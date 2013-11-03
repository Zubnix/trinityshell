package org.trinity.foundation.display.x11.impl;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_get_geometry_cookie_t;
import org.freedesktop.xcb.xcb_get_geometry_reply_t;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.shared.Rectangle;
import org.trinity.foundation.display.x11.api.XConnection;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_config_window_t.*;
import static org.freedesktop.xcb.xcb_input_focus_t.XCB_INPUT_FOCUS_NONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class})
public class TestXWindow {
    @Mock
    private XTime xTime;
    @Mock
    private XConnection xConnection;
    @Mock
    private SWIGTYPE_p_xcb_connection_t xcb_connection;

    private final Integer nativeHandle = 123;
    @Mock
    private DisplaySurfaceHandle displaySurfaceHandle;
    @Mock
    private ListeningExecutorService xExecutor;
    @InjectMocks
    private XWindow xWindow;

    @Before
    public void setup() {
        when(xConnection.getConnectionReference()).thenReturn(xcb_connection);
        when(xExecutor.submit(Matchers.<Callable>any())).thenAnswer(invocation -> {
            Object arg0 = invocation.getArguments()[0];
            Callable<?> submittedCallable = (Callable<?>) arg0;
            Object result = submittedCallable.call();
            return Futures.immediateFuture(result);
        });
        when(displaySurfaceHandle.getNativeHandle()).thenReturn(nativeHandle);
    }

    @Test
    public void testDestroy() {
        //given
        //an XWindow
        mockStatic(LibXcb.class);

        //when
        //the XWindow is destroyed
        xWindow.destroy();

        //then
        //the native X server window is destroyed
        verifyStatic();
        xcb_destroy_window(xcb_connection,
                           nativeHandle);
        xcb_flush(xcb_connection);
    }

    @Test
    public void testGiveInputFocus() {
        //given
        //an XWindow
        mockStatic(LibXcb.class);

        //when
        //the XWindow is given the focus
        xWindow.setInputFocus();

        //then
        //the native X server window should receive the focus
        verifyStatic();
        xcb_set_input_focus(xcb_connection,
                            (short) XCB_INPUT_FOCUS_NONE,
                            nativeHandle,
                            xTime.getTime());
        xcb_flush(xcb_connection);
    }

    @Test
    public void testMove() {
        //given
        //an XWindow
        mockStatic(LibXcb.class);

        //when
        //the XWindow is moved
        int x = 567;
        int y = 890;
        xWindow.move(x,
                     y);

        //then
        //the native X server window should be moved
        verifyStatic();
        final int MOVE_VALUE_MASK = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y;
        ArgumentCaptor<ByteBuffer> byteBufferArgumentCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        xcb_configure_window(eq(xcb_connection),
                             eq(nativeHandle),
                             eq(MOVE_VALUE_MASK),
                             byteBufferArgumentCaptor.capture());
        xcb_flush(xcb_connection);
        ByteBuffer value = byteBufferArgumentCaptor.getValue();
        int passedX = value.getInt(0);
        int passedY = value.getInt(4);
        assertEquals(x,
                     passedX);
        assertEquals(y,
                     passedY);
    }

    @Test
    public void testMoveResizeWithXWindowBorder() {
        //given
        //an XWindow with a border width
        mockStatic(LibXcb.class);
        xcb_get_geometry_cookie_t geo_cookie = mock(xcb_get_geometry_cookie_t.class);
        xcb_get_geometry_reply_t geo_reply = mock(xcb_get_geometry_reply_t.class);
        int borderWidth = 3;

        when(xcb_get_geometry(xcb_connection,
                              nativeHandle)).thenReturn(geo_cookie);
        when(xcb_get_geometry_reply(eq(xcb_connection),
                                    eq(geo_cookie),
                                    any())).thenReturn(geo_reply);
        when(geo_reply.getBorder_width()).thenReturn(borderWidth);

        //when
        //the XWindow is moved and resized
        int width = 123;
        int height = 456;
        int x = 789;
        int y = 1011;

        xWindow.moveResize(x,
                           y,
                           width,
                           height);

        //then
        //the native X server window is moved and resized
        final int MOVE_RESIZE_VALUE_MASK = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y
                | XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT;
        ArgumentCaptor<ByteBuffer> byteBufferArgumentCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        verifyStatic();
        xcb_configure_window(eq(xcb_connection),
                             eq(nativeHandle),
                             eq(MOVE_RESIZE_VALUE_MASK),
                             byteBufferArgumentCaptor.capture());
        xcb_flush(xcb_connection);
        ByteBuffer value = byteBufferArgumentCaptor.getValue();
        int passedX = value.getInt(0);
        int passedY = value.getInt(4);
        int passedWidth = value.getInt(8);
        int passedHeight = value.getInt(12);

        assertEquals(x,
                     passedX);
        assertEquals(y,
                     passedY);
        assertEquals(width - (2 * borderWidth),
                     passedWidth);
        assertEquals(height - (2 * borderWidth),
                     passedHeight);
    }

    @Test
    public void testResizeWithXWindowBorder() {
        //given
        //an XWindow
        mockStatic(LibXcb.class);
        xcb_get_geometry_cookie_t geo_cookie = mock(xcb_get_geometry_cookie_t.class);
        xcb_get_geometry_reply_t geo_reply = mock(xcb_get_geometry_reply_t.class);
        int borderWidth = 4;

        when(xcb_get_geometry(xcb_connection,
                              nativeHandle)).thenReturn(geo_cookie);
        when(xcb_get_geometry_reply(eq(xcb_connection),
                                    eq(geo_cookie),
                                    any())).thenReturn(geo_reply);
        when(geo_reply.getBorder_width()).thenReturn(borderWidth);

        //when
        //the XWindow is resized
        int width = 12;
        int height = 34;

        xWindow.resize(width,
                       height);

        //then
        //the native X server window is resized, taking into account it's X window border.
        final int RESIZE_VALUE_MASK = XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT;
        ArgumentCaptor<ByteBuffer> byteBufferArgumentCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        verifyStatic();
        xcb_configure_window(eq(xcb_connection),
                             eq(nativeHandle),
                             eq(RESIZE_VALUE_MASK),
                             byteBufferArgumentCaptor.capture());
        xcb_flush(xcb_connection);
        ByteBuffer value = byteBufferArgumentCaptor.getValue();
        int passedWidth = value.getInt(0);
        int passedHeight = value.getInt(4);

        assertEquals(width - (2 * borderWidth),
                     passedWidth);
        assertEquals(height - (2 * borderWidth),
                     passedHeight);
    }

    @Test
    public void testGeometry() throws ExecutionException, InterruptedException {
        //given
        //and XWindow
        mockStatic(LibXcb.class);
        xcb_get_geometry_cookie_t geo_cookie = mock(xcb_get_geometry_cookie_t.class);
        xcb_get_geometry_reply_t geo_reply = mock(xcb_get_geometry_reply_t.class);
        int borderWidth = 5;
        int givenX = 321;
        int givenY = 112;
        int givenWidth = 987;
        int givenHeight = 654;

        when(xcb_get_geometry(xcb_connection,
                              nativeHandle)).thenReturn(geo_cookie);
        when(xcb_get_geometry_reply(eq(xcb_connection),
                                    eq(geo_cookie),
                                    any())).thenReturn(geo_reply);
        when(geo_reply.getBorder_width()).thenReturn(borderWidth);
        when(geo_reply.getX()).thenReturn((short) givenX);
        when(geo_reply.getY()).thenReturn((short) givenY);
        when(geo_reply.getWidth()).thenReturn(givenWidth);
        when(geo_reply.getHeight()).thenReturn(givenHeight);

        //when
        //the XWindow's geometry is requested
        ListenableFuture<Rectangle> geometry = xWindow.getGeometry();

        //then
        //the native X server window's geometry is returned.
        verifyStatic();
        xcb_get_geometry_reply(eq(xcb_connection),
                               eq(geo_cookie),
                               any());

        Rectangle rectangle = geometry.get();
        assertEquals(givenX,
                     rectangle.getPosition().getX());
        assertEquals(givenY,
                     rectangle.getPosition().getY());
        assertEquals(givenWidth + 2 * borderWidth,
                     rectangle.getSize().getWidth());
        assertEquals(givenHeight + 2 * borderWidth,
                     rectangle.getSize().getHeight());


    }
}

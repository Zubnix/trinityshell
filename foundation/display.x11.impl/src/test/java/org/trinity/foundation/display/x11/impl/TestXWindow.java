package org.trinity.foundation.display.x11.impl;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
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

import javax.media.nativewindow.util.Rectangle;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_config_window_t.*;
import static org.freedesktop.xcb.xcb_input_focus_t.XCB_INPUT_FOCUS_NONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class})
public class TestXWindow {
	@Mock
	private XTime                       xTime;
	@Mock
	private XEventChannel               xEventChannel;
	@Mock
	private SWIGTYPE_p_xcb_connection_t xcb_connection;

	private final Integer nativeHandle = 123;
	@Mock
	private DisplaySurfaceHandle     displaySurfaceHandle;
	@Mock
	private ListeningExecutorService xExecutor;
	@InjectMocks
	private XWindow                  xWindow;

	@Before
	public void setup() {
		when(this.xEventChannel.getXcbConnection()).thenReturn(this.xcb_connection);
		when(this.xExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object arg0 = invocation.getArguments()[0];
				final Callable<?> submittedCallable = (Callable<?>) arg0;
				final Object result = submittedCallable.call();
				return Futures.immediateFuture(result);
			}
		});

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Object arg0 = invocation.getArguments()[0];
				final Runnable submittedRunnable = (Runnable) arg0;
				submittedRunnable.run();
				return null;
			}
		}).when(this.xExecutor).execute(Matchers.<Runnable>any());
		when(this.displaySurfaceHandle.getNativeHandle()).thenReturn(this.nativeHandle);
	}

	@Test
	public void testDestroy() {
		//given
		//an XWindow
		mockStatic(LibXcb.class);

		//when
		//the XWindow is destroyed
		this.xWindow.destroy();

		//then
		//the native X server window is destroyed
		verifyStatic();
		xcb_destroy_window(this.xcb_connection,
						   this.nativeHandle);
		xcb_flush(this.xcb_connection);
	}

	@Test
	public void testGiveInputFocus() {
		//given
		//an XWindow
		mockStatic(LibXcb.class);

		//when
		//the XWindow is given the focus
		this.xWindow.setInputFocus();

		//then
		//the native X server window should receive the focus
		verifyStatic();
		xcb_set_input_focus(this.xcb_connection,
							(short) XCB_INPUT_FOCUS_NONE,
							this.nativeHandle,
							this.xTime.getTime());
		xcb_flush(this.xcb_connection);
	}

	@Test
	public void testMove() {
		//given
		//an XWindow
		mockStatic(LibXcb.class);

		//when
		//the XWindow is moved
		final int x = 567;
		final int y = 890;
		this.xWindow.move(x,
						  y);

		//then
		//the native X server window should be moved
		verifyStatic();
        final int MOVE_VALUE_MASK = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y;
        final ArgumentCaptor<ByteBuffer> byteBufferArgumentCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        xcb_configure_window(eq(this.xcb_connection),
                eq(this.nativeHandle),
                eq(MOVE_VALUE_MASK),
                byteBufferArgumentCaptor.capture());
        xcb_flush(this.xcb_connection);
        final ByteBuffer value = byteBufferArgumentCaptor.getValue();
        final int passedX = value.getInt(0);
        final int passedY = value.getInt(4);
        assertEquals(x,
                passedX);
        assertEquals(y,
                passedY);
    }

    @Test
    public void testResizeWithXWindowBorder() {
        //given
        //an XWindow
        mockStatic(LibXcb.class);
        final xcb_get_geometry_cookie_t geo_cookie = mock(xcb_get_geometry_cookie_t.class);
        final xcb_get_geometry_reply_t geo_reply = mock(xcb_get_geometry_reply_t.class);
        final int borderWidth = 4;

        when(xcb_get_geometry(this.xcb_connection,
							  this.nativeHandle)).thenReturn(geo_cookie);
        when(xcb_get_geometry_reply(eq(this.xcb_connection),
                eq(geo_cookie),
                (xcb_generic_error_t) any())).thenReturn(geo_reply);
        when(geo_reply.getBorder_width()).thenReturn(borderWidth);

        //when
        //the XWindow is resized
        final int width = 12;
        final int height = 34;

		this.xWindow.resize(width,
							height);

        //then
        //the native X server window is resized, taking into account it's X window border.
        final int RESIZE_VALUE_MASK = XCB_CONFIG_WINDOW_WIDTH | XCB_CONFIG_WINDOW_HEIGHT;
        final ArgumentCaptor<ByteBuffer> byteBufferArgumentCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        verifyStatic();
        xcb_configure_window(eq(this.xcb_connection),
                eq(this.nativeHandle),
                eq(RESIZE_VALUE_MASK),
                byteBufferArgumentCaptor.capture());
        xcb_flush(this.xcb_connection);
        final ByteBuffer value = byteBufferArgumentCaptor.getValue();
        final int passedWidth = value.getInt(0);
        final int passedHeight = value.getInt(4);

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
        final xcb_get_geometry_cookie_t geo_cookie = mock(xcb_get_geometry_cookie_t.class);
        final xcb_get_geometry_reply_t geo_reply = mock(xcb_get_geometry_reply_t.class);
        final Integer borderWidth = 5;
        final Integer givenX = 321;
        final Integer givenY = 112;
        final Integer givenWidth = 987;
        final Integer givenHeight = 654;

        when(xcb_get_geometry(this.xcb_connection,
							  this.nativeHandle)).thenReturn(geo_cookie);
        when(xcb_get_geometry_reply(eq(this.xcb_connection),
                eq(geo_cookie),
                (xcb_generic_error_t) any())).thenReturn(geo_reply);
        when(geo_reply.getBorder_width()).thenReturn(borderWidth);
        when(geo_reply.getX()).thenReturn(givenX.shortValue());
        when(geo_reply.getY()).thenReturn(givenY.shortValue());
        when(geo_reply.getWidth()).thenReturn(givenWidth);
        when(geo_reply.getHeight()).thenReturn(givenHeight);

        //when
        //the XWindow's geometry is requested
        final Rectangle geometry = this.xWindow.getShape();

        //then
        //the native X server window's geometry is returned.
        verifyStatic();
        xcb_get_geometry_reply(eq(this.xcb_connection),
                eq(geo_cookie),
                (xcb_generic_error_t) any());

        final Rectangle rectangle = geometry;
		assertEquals((int) givenX,
					 rectangle.getX());
		assertEquals((int) givenY,
					 rectangle.getY());
		assertEquals((int) (givenWidth + 2 * borderWidth),
					 rectangle.getWidth());
		assertEquals((int) (givenHeight + 2 * borderWidth),
					 rectangle.getHeight());
	}
}

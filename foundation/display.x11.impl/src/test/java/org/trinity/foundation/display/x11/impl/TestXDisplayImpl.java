package org.trinity.foundation.display.x11.impl;

import com.google.common.util.concurrent.ListeningExecutorService;
import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_query_tree_cookie_t;
import org.freedesktop.xcb.xcb_query_tree_reply_t;
import org.freedesktop.xcb.xcb_screen_iterator_t;
import org.freedesktop.xcb.xcb_screen_t;
import org.freedesktop.xcb.xcb_setup_t;
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

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LibXcb.class)
public class TestXDisplayImpl {

    @Mock
    private SWIGTYPE_p_xcb_connection_t xcb_connection;
    @Mock
    private XConnection xConnection;
    @Mock
    private XWindowPoolImpl xWindowPool;
    @Mock
    private ListeningExecutorService xExecutor;

    @Before
    public void setup() {
        when(xExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                Object arg0 = invocation.getArguments()[0];
                Callable<?> submittedCallable = (Callable<?>) arg0;
                submittedCallable.call();
                return null;
            }
        });

        when(xConnection.getConnectionReference()).thenReturn(xcb_connection);
    }

    @Test(expected = Error.class)
    public void testXDisplayConstructionXError() throws ExecutionException, InterruptedException {
        //given
        //an underlying X server with errors
        mockStatic(LibXcb.class);
        when(xcb_connection_has_error(xcb_connection)).thenReturn(1);
        //when
        //a new XDisplay is created
        XDisplayImpl xDisplay = new XDisplayImpl(xConnection,
                                                 xWindowPool,
                                                 xExecutor);
        //then
        //the XDisplay object throws an Error
    }

    @Test
    public void testXDisplayConstructionHappy() throws ExecutionException, InterruptedException {
        //given
        //an underlying X server without errors
        //an underlying X server with a single screen
        //an underlying X server with several clients
        mockStatic(LibXcb.class);

        xcb_setup_t xcb_setup = mock(xcb_setup_t.class);
        xcb_screen_iterator_t xcb_screen_iterator = mock(xcb_screen_iterator_t.class);
        xcb_screen_t xcb_screen = mock(xcb_screen_t.class);
        int rootWindowId = 123;

        when(xcb_connection_has_error(xcb_connection)).thenReturn(0);
        when(xcb_get_setup(xcb_connection)).thenReturn(xcb_setup);
        when(xcb_setup_roots_iterator(xcb_setup)).thenReturn(xcb_screen_iterator);
        when(xcb_screen_iterator.getData()).thenReturn(xcb_screen);
        when(xcb_screen_iterator.getRem()).thenReturn(1,
                                                      0);
        when(xcb_screen.getRoot()).thenReturn(rootWindowId);
        doNothing().when(xcb_change_window_attributes(eq(xcb_connection),
                                                      eq(rootWindowId),
                                                      eq(XCB_CW_EVENT_MASK),
                                                      (ByteBuffer) any()));
        doNothing().when(xcb_flush(xcb_connection));

        xcb_query_tree_cookie_t xcb_query_tree_cookie = mock(xcb_query_tree_cookie_t.class);
        xcb_query_tree_reply_t xcb_query_tree_reply = mock(xcb_query_tree_reply_t.class);

        when(xcb_query_tree(xcb_connection,
                            rootWindowId)).thenReturn(xcb_query_tree_cookie);
        when(xcb_query_tree_reply(eq(xcb_connection),
                                  eq(xcb_query_tree_cookie),
                                  (xcb_generic_error_t) any())).thenReturn(xcb_query_tree_reply);
        //TODO more mocking

        //when
        //a new XDisplay is created
        XDisplayImpl xDisplay = new XDisplayImpl(xConnection,
                                                 xWindowPool,
                                                 xExecutor);
        //then
        //the root window is configured
        //the clients are discovered
        //the clients are configured
        //client destruction is tracked
    }
}

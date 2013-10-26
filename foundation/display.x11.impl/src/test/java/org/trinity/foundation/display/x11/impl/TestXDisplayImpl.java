package org.trinity.foundation.display.x11.impl;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_get_window_attributes_cookie_t;
import org.freedesktop.xcb.xcb_get_window_attributes_reply_t;
import org.freedesktop.xcb.xcb_query_tree_cookie_t;
import org.freedesktop.xcb.xcb_query_tree_reply_t;
import org.freedesktop.xcb.xcb_screen_iterator_t;
import org.freedesktop.xcb.xcb_screen_t;
import org.freedesktop.xcb.xcb_setup_t;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.event.DestroyNotify;
import org.trinity.foundation.api.display.event.DisplaySurfaceCreationNotify;
import org.trinity.foundation.display.x11.api.XConnection;
import org.trinity.foundation.display.x11.api.XWindowHandle;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.*;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.*;
import static org.freedesktop.xcb.xcb_map_state_t.XCB_MAP_STATE_UNMAPPED;
import static org.freedesktop.xcb.xcb_map_state_t.XCB_MAP_STATE_VIEWABLE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;

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
                Object result = submittedCallable.call();
                return Futures.immediateFuture(result);
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
        new XDisplayImpl(xConnection,
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

        xcb_query_tree_cookie_t xcb_query_tree_cookie = mock(xcb_query_tree_cookie_t.class);
        xcb_query_tree_reply_t xcb_query_tree_reply = mock(xcb_query_tree_reply_t.class);
        ByteBuffer treeChildren = mock(ByteBuffer.class);
        int nroChildren = 3;
        int childId0 = 2;
        int childId1 = 4;
        int childId2 = 6;

        when(xcb_query_tree(xcb_connection,
                            rootWindowId)).thenReturn(xcb_query_tree_cookie);
        when(xcb_query_tree_reply(eq(xcb_connection),
                                  eq(xcb_query_tree_cookie),
                                  (xcb_generic_error_t) any())).thenReturn(xcb_query_tree_reply);
        when(xcb_query_tree_children(xcb_query_tree_reply)).thenReturn(treeChildren);
        when(xcb_query_tree_children_length(xcb_query_tree_reply)).thenReturn(nroChildren);
        when(treeChildren.getInt()).thenReturn(childId0,
                                               childId1,
                                               childId2);

        xcb_get_window_attributes_cookie_t get_window_attributes_cookie = mock(xcb_get_window_attributes_cookie_t.class);
        xcb_get_window_attributes_reply_t get_window_attributes_reply = mock(xcb_get_window_attributes_reply_t.class);

        when(xcb_get_window_attributes(eq(xcb_connection),
                                       anyInt())).thenReturn(get_window_attributes_cookie);
        when(xcb_get_window_attributes_reply(eq(xcb_connection),
                                             eq(get_window_attributes_cookie),
                                             (xcb_generic_error_t) any())).thenReturn(get_window_attributes_reply);
        when(get_window_attributes_reply.getOverride_redirect()).thenReturn((short) 1,
                                                                            (short) 0,
                                                                            (short) 0);
        when(get_window_attributes_reply.getMap_state()).thenReturn((short) XCB_MAP_STATE_VIEWABLE,
                                                                    (short) XCB_MAP_STATE_UNMAPPED,
                                                                    (short) XCB_MAP_STATE_VIEWABLE);

        DisplaySurface clientWindow = mock(DisplaySurface.class);
        DisplaySurfaceHandle displaySurfaceHandle = mock(DisplaySurfaceHandle.class);

        when(xWindowPool.getDisplaySurface((DisplaySurfaceHandle) any())).thenReturn(clientWindow);
        when(clientWindow.getDisplaySurfaceHandle()).thenReturn(displaySurfaceHandle);
        when(displaySurfaceHandle.getNativeHandle()).thenReturn(6);

        DisplaySurface newClient = mock(DisplaySurface.class);
        DisplaySurfaceCreationNotify displaySurfaceCreationNotify = mock(DisplaySurfaceCreationNotify.class);

        when(displaySurfaceCreationNotify.getDisplaySurface()).thenReturn(newClient);

        //when
        //a new XDisplay is created
        //a client is created
        XDisplayImpl xDisplay = new XDisplayImpl(xConnection,
                                                 xWindowPool,
                                                 xExecutor);
        xDisplay.post(displaySurfaceCreationNotify);

        //then
        //the root window is configured
        //the clients are discovered
        //the clients are configured
        //client destruction is tracked
        //client construction is tracked
        final ByteBuffer rootWindowAttributes = allocateDirect(4).order(nativeOrder())
                .putInt(XCB_EVENT_MASK_PROPERTY_CHANGE | XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT);
        verifyStatic();
        xcb_change_window_attributes(eq(xcb_connection),
                                     eq(rootWindowId),
                                     eq(XCB_CW_EVENT_MASK),
                                     eq(rootWindowAttributes));

        XWindowHandle clientHandle = new XWindowHandle(6);
        verify(xWindowPool).getDisplaySurface(eq(clientHandle));
        verifyNoMoreInteractions(xWindowPool);
        assertTrue(xDisplay.getDisplaySurfaces().get().get(0) == clientWindow);

        final int CLIENT_EVENT_MASK = XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW
                | XCB_EVENT_MASK_STRUCTURE_NOTIFY;
        final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder())
                .putInt(CLIENT_EVENT_MASK);
        verifyStatic();
        xcb_change_window_attributes(eq(xcb_connection),
                                     eq(6),
                                     eq(XCB_CW_EVENT_MASK),
                                     eq(CLIENT_EVENTS_CONFIG_BUFFER));

        ArgumentCaptor<Object> destroyListenerCaptor = ArgumentCaptor.forClass(Object.class);
        verify(clientWindow).register(destroyListenerCaptor.capture());
        Object destroyListener = destroyListenerCaptor.getValue();
        Method listenerMethod = null;
        for(Method method : destroyListener.getClass().getMethods()) {
            if(method.getAnnotation(Subscribe.class) != null && method.getParameterTypes().length == 1 && method
                    .getParameterTypes()[0].equals(DestroyNotify.class)) {
                listenerMethod = method;
                break;
            }
        }
        assertNotNull(listenerMethod);

        assertTrue(xDisplay.getDisplaySurfaces().get().get(1) == newClient);
    }
}

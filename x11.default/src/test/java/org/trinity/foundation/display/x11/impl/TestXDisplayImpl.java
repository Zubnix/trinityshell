package org.trinity.foundation.display.x11.impl;

import com.google.common.eventbus.Subscribe;
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
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.display.api.DisplaySurface;
import org.trinity.display.api.DisplaySurfaceHandle;
import org.trinity.display.api.event.DestroyNotify;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import static java.nio.ByteBuffer.allocateDirect;
import static java.nio.ByteOrder.nativeOrder;
import static org.freedesktop.xcb.LibXcb.xcb_change_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_connection_has_error;
import static org.freedesktop.xcb.LibXcb.xcb_get_setup;
import static org.freedesktop.xcb.LibXcb.xcb_get_window_attributes;
import static org.freedesktop.xcb.LibXcb.xcb_get_window_attributes_reply;
import static org.freedesktop.xcb.LibXcb.xcb_query_tree;
import static org.freedesktop.xcb.LibXcb.xcb_query_tree_children;
import static org.freedesktop.xcb.LibXcb.xcb_query_tree_children_length;
import static org.freedesktop.xcb.LibXcb.xcb_query_tree_reply;
import static org.freedesktop.xcb.LibXcb.xcb_setup_roots_iterator;
import static org.freedesktop.xcb.xcb_cw_t.XCB_CW_EVENT_MASK;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_ENTER_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_LEAVE_WINDOW;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_PROPERTY_CHANGE;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_STRUCTURE_NOTIFY;
import static org.freedesktop.xcb.xcb_event_mask_t.XCB_EVENT_MASK_SUBSTRUCTURE_REDIRECT;
import static org.freedesktop.xcb.xcb_map_state_t.XCB_MAP_STATE_UNMAPPED;
import static org.freedesktop.xcb.xcb_map_state_t.XCB_MAP_STATE_VIEWABLE;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LibXcb.class)
public class TestXDisplayImpl {

	@Mock
	private SWIGTYPE_p_xcb_connection_t xcb_connection;
	@Mock
	private XEventChannel               xEventChannel;
	@Mock
	private DisplaySurfacePool          xWindowPool;

	@Before
	public void setup() {

		when(this.xEventChannel.getXcbConnection()).thenReturn(this.xcb_connection);
	}

	@Test(expected = Error.class)
	public void testXDisplayConstructionXError() throws ExecutionException, InterruptedException {
		//FIXME move to xeventchannel
		//given
		//an underlying X server with errors
		mockStatic(LibXcb.class);
		when(xcb_connection_has_error(this.xcb_connection)).thenReturn(1);
		//when
		//a new XDisplay is created
		new XCompositor(null);
		//then
		//the XDisplay object throws an Error
	}

	@Test
	public void testXDisplayConstructionHappy() throws ExecutionException, InterruptedException {
		//FIXME move to xeventchannel

		//given
		//an underlying X server without errors
		//an underlying X server with a single screen
		//an underlying X server with several clients
		mockStatic(LibXcb.class);

		final xcb_setup_t xcb_setup = mock(xcb_setup_t.class);
		final xcb_screen_iterator_t xcb_screen_iterator = mock(xcb_screen_iterator_t.class);
		final xcb_screen_t xcb_screen = mock(xcb_screen_t.class);
		final int rootWindowId = 123;

		when(xcb_connection_has_error(this.xcb_connection)).thenReturn(0);
		when(xcb_get_setup(this.xcb_connection)).thenReturn(xcb_setup);
		when(xcb_setup_roots_iterator(xcb_setup)).thenReturn(xcb_screen_iterator);
		when(xcb_screen_iterator.getData()).thenReturn(xcb_screen);
		when(xcb_screen_iterator.getRem()).thenReturn(1,
													  0);
		when(xcb_screen.getRoot()).thenReturn(rootWindowId);

		final xcb_query_tree_cookie_t xcb_query_tree_cookie = mock(xcb_query_tree_cookie_t.class);
		final xcb_query_tree_reply_t xcb_query_tree_reply = mock(xcb_query_tree_reply_t.class);
		final ByteBuffer treeChildren = mock(ByteBuffer.class);
		final int nroChildren = 3;
		final int childId0 = 2;
		final int childId1 = 4;
		final int childId2 = 6;

		when(xcb_query_tree(this.xcb_connection,
							rootWindowId)).thenReturn(xcb_query_tree_cookie);
		when(xcb_query_tree_reply(eq(this.xcb_connection),
								  eq(xcb_query_tree_cookie),
								  (xcb_generic_error_t) any())).thenReturn(xcb_query_tree_reply);
        when(xcb_query_tree_children(xcb_query_tree_reply)).thenReturn(treeChildren);
        when(xcb_query_tree_children_length(xcb_query_tree_reply)).thenReturn(nroChildren);
        when(treeChildren.getInt()).thenReturn(childId0,
                                               childId1,
                                               childId2);

        final xcb_get_window_attributes_cookie_t get_window_attributes_cookie = mock(xcb_get_window_attributes_cookie_t.class);
        final xcb_get_window_attributes_reply_t get_window_attributes_reply = mock(xcb_get_window_attributes_reply_t.class);

		when(xcb_get_window_attributes(eq(this.xcb_connection),
									   anyInt())).thenReturn(get_window_attributes_cookie);
		when(xcb_get_window_attributes_reply(eq(this.xcb_connection),
											 eq(get_window_attributes_cookie),
                (xcb_generic_error_t) any())).thenReturn(get_window_attributes_reply);
        when(get_window_attributes_reply.getOverride_redirect()).thenReturn((short) 1,
                                                                            (short) 0,
                                                                            (short) 0);
        when(get_window_attributes_reply.getMap_state()).thenReturn((short) XCB_MAP_STATE_VIEWABLE,
                                                                    (short) XCB_MAP_STATE_UNMAPPED,
                                                                    (short) XCB_MAP_STATE_VIEWABLE);

        final DisplaySurface clientWindow = mock(DisplaySurface.class);
        final DisplaySurfaceHandle displaySurfaceHandle = mock(DisplaySurfaceHandle.class);

		when(this.xWindowPool.get((DisplaySurfaceHandle) any())).thenReturn(clientWindow);
		when(clientWindow.getDisplaySurfaceHandle()).thenReturn(displaySurfaceHandle);
        when(displaySurfaceHandle.getNativeHandle()).thenReturn(6);

        final DisplaySurface newClient = mock(DisplaySurface.class);
        final DisplaySurfaceCreationNotify displaySurfaceCreationNotify = mock(DisplaySurfaceCreationNotify.class);

        when(displaySurfaceCreationNotify.getDisplaySurface()).thenReturn(newClient);

        //when
        //a new XDisplay is created
        //a client is created
		final XCompositor xDisplay = new XCompositor(null);
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
		xcb_change_window_attributes(eq(this.xcb_connection),
									 eq(rootWindowId),
                                     eq(XCB_CW_EVENT_MASK),
                                     eq(rootWindowAttributes));

        final DisplaySurfaceHandle clientHandle = XWindowHandle.create(6);
		verify(this.xWindowPool).get(eq(clientHandle));
		verifyNoMoreInteractions(this.xWindowPool);

        final int CLIENT_EVENT_MASK = XCB_EVENT_MASK_ENTER_WINDOW | XCB_EVENT_MASK_LEAVE_WINDOW
                | XCB_EVENT_MASK_STRUCTURE_NOTIFY;
        final ByteBuffer CLIENT_EVENTS_CONFIG_BUFFER = allocateDirect(4).order(nativeOrder())
                .putInt(CLIENT_EVENT_MASK);
        verifyStatic();
		xcb_change_window_attributes(eq(this.xcb_connection),
									 eq(6),
                                     eq(XCB_CW_EVENT_MASK),
                                     eq(CLIENT_EVENTS_CONFIG_BUFFER));

        final ArgumentCaptor<Object> destroyListenerCaptor = ArgumentCaptor.forClass(Object.class);
        verify(clientWindow).register(destroyListenerCaptor.capture());
        final Object destroyListener = destroyListenerCaptor.getValue();
        Method listenerMethod = null;
        for(final Method method : destroyListener.getClass().getMethods()) {
            if(method.getAnnotation(Subscribe.class) != null && method.getParameterTypes().length == 1 && method
                    .getParameterTypes()[0].equals(DestroyNotify.class)) {
                listenerMethod = method;
                break;
            }
        }
        assertNotNull(listenerMethod);
    }
}

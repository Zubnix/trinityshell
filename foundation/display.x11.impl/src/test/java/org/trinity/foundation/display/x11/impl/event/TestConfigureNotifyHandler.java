package org.trinity.foundation.display.x11.impl.event;


import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.xcb_configure_notify_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.DisplaySurfaceHandle;
import org.trinity.foundation.api.display.event.GeometryNotify;
import org.trinity.foundation.display.x11.api.XWindowHandle;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePoolImpl;

import static org.freedesktop.xcb.LibXcbJNI.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LibXcbJNI.class)
public class TestConfigureNotifyHandler {

    @Mock
    private EventBus xEventBus;
    @Mock
    private DisplaySurfacePoolImpl xWindowPool;
    @InjectMocks
    private ConfigureNotifyHandler configureNotifyHandler;

    @Mock
    private xcb_generic_event_t xcb_generic_event;

    private final int targetWindowId = 123;

    @Test
    public void testEventHandling() {
        //given
        //a ConfigureNotifyHandler
        //an xcb_generic_event_t
        mockStatic(LibXcbJNI.class);

        final Short x = 2;
        final Short y = 4;
        final Integer width = 10;
        final Integer height = 20;
        final Integer border = 7;
        when(xcb_configure_notify_event_t_x_get(anyLong(),
                (xcb_configure_notify_event_t) any())).thenReturn(x);
        when(xcb_configure_notify_event_t_y_get(anyLong(),
                (xcb_configure_notify_event_t) any())).thenReturn(y);
        when(xcb_configure_notify_event_t_width_get(anyLong(),
                (xcb_configure_notify_event_t) any())).thenReturn(width);
        when(xcb_configure_notify_event_t_height_get(anyLong(),
                (xcb_configure_notify_event_t) any()))
                .thenReturn(height);
        when(xcb_configure_notify_event_t_border_width_get(anyLong(),
                (xcb_configure_notify_event_t) any()))
                .thenReturn(border);

        //when
        //an xcb_generic_event_t event arrives
        final Optional<GeometryNotify> stackingChangedNotifyOptional = configureNotifyHandler
                .handle(xcb_generic_event);

        //then
        //the xcb_circulate_notify_event_t is posted on the x event bus
        //the event is converted to a StackingChangedNotify
        verify(xEventBus).post(isA(xcb_configure_notify_event_t.class));
        assertTrue(stackingChangedNotifyOptional.isPresent());
        assertEquals(x,
                stackingChangedNotifyOptional.get().getGeometry().getPosition().getX());
        assertEquals(y,
                stackingChangedNotifyOptional.get().getGeometry().getPosition().getY());
        assertEquals((Integer) (width + 2 * border),
                stackingChangedNotifyOptional.get().getGeometry().getSize().getWidth());
        assertEquals((Integer) (height + 2 * border),
                stackingChangedNotifyOptional.get().getGeometry().getSize().getHeight());
    }

    @Test
    public void testGetTarget() {
        //given
        //a ConfigureNotifyHandler
        //an xcb_generic_event_t
        mockStatic(LibXcbJNI.class);
        when(xcb_configure_notify_event_t_window_get(anyLong(),
                (xcb_configure_notify_event_t) any())).thenReturn(targetWindowId);

        final DisplaySurface displaySurface = mock(DisplaySurface.class);
        when(displaySurface.getDisplaySurfaceHandle()).thenReturn(XWindowHandle.create(targetWindowId));
        when(xWindowPool.getDisplaySurface((DisplaySurfaceHandle) any())).thenAnswer(new Answer<Object>() {
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
        final Optional<DisplaySurface> target = configureNotifyHandler.getTarget(xcb_generic_event);

        //then
        //the correct DisplaySurface is returned
        final ArgumentCaptor<XWindowHandle> windowHandleArgumentCaptor = ArgumentCaptor.forClass(XWindowHandle.class);
        verify(xWindowPool).getDisplaySurface(windowHandleArgumentCaptor.capture());
        assertEquals((Integer) targetWindowId,
                windowHandleArgumentCaptor.getValue().getNativeHandle());
        assertTrue(target.isPresent());
    }
}

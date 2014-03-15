package org.trinity.foundation.display.x11.impl.xeventhandler;


import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.xcb_generic_error_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.display.api.DisplaySurface;
import org.trinity.display.api.event.DisplayEvent;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcbJNI.class})
public class TestGenericErrorHandler {
    @Mock
    private EventBus xEventBus;
    @InjectMocks
    private GenericErrorHandler genericErrorHandler;
    @Mock
    private xcb_generic_event_t xcb_generic_event;

    @Test
    public void testEventHandling() {
        //given
        //a GenericErrorHandler
        //an xcb_generic_event_t
        mockStatic(LibXcbJNI.class);

        //when
        //an xcb_generic_event_t event arrives
		final Optional<DisplayEvent> handle = this.genericErrorHandler.handle(this.xcb_generic_event);

		//then
        //the xcb_generic_error_t is posted on the x event bus
        //the event is not converted to a DisplayEvent
		verify(this.xEventBus).post(isA(xcb_generic_error_t.class));
		assertFalse(handle.isPresent());
    }

    @Test
    public void testGetTarget() {
        //given
        //a GenericErrorHandler
        //an xcb_generic_event_t
        mockStatic(LibXcbJNI.class);

        //when
        //the target of the xcb_generic_event_t event is requested
		final Optional<DisplaySurface> target = this.genericErrorHandler.getTarget(this.xcb_generic_event);

		//then
        //an absent target is returned
        assertFalse(target.isPresent());
    }
}

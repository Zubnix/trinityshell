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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.api.display.Compositor;
import org.trinity.foundation.api.display.event.ShowRequest;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePool;
import org.trinity.foundation.display.x11.impl.XEventChannel;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class,
        LibXcbJNI.class})
public class TestMapRequestHandler {
	@Mock
	private EventBus           xEventBus;
	@Mock
	private XEventChannel      xEventChannel;
	@Mock
	private DisplaySurfacePool xWindowPool;
	@Mock
	private Compositor         compositor;
	@InjectMocks
	private MapRequestHandler  mapRequestHandler;

	@Mock
	private xcb_generic_event_t         xcb_generic_event;
	@Mock
	private SWIGTYPE_p_xcb_connection_t xcb_connection;

	private final int targetWindowId = 123;

	@Before
	public void setup() {
		when(this.xEventChannel.getXcbConnection()).thenReturn(this.xcb_connection);
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
		final Optional<ShowRequest> showRequestOptional = this.mapRequestHandler.handle(this.xcb_generic_event);

		//then
		//the xcb_map_request_event_t is posted on the x event bus
		//the event is converted to a ShowRequest
		verify(this.xEventBus).post(isA(xcb_map_request_event_t.class));
		assertTrue(showRequestOptional.isPresent());
	}
}
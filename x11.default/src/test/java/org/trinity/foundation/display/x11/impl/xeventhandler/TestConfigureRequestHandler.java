package org.trinity.foundation.display.x11.impl.xeventhandler;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.display.x11.impl.XEventChannel;
import org.trinity.foundation.display.x11.impl.XSurfacePool;

import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class,
                 LibXcbJNI.class})
public class TestConfigureRequestHandler {

	@Mock
	private XEventChannel           xEventChannel;
	@Mock
	private XSurfacePool            xSurfacePool;
	@InjectMocks
	private ConfigureRequestHandler configureRequestHandler;

	@Mock
	private xcb_generic_event_t         xcb_generic_event;

	@Test
	public void testEventHandling() {
		//given
		//a ConfigureRequestHandler
		//an xcb_generic_event_t

		//when
		//an xcb_generic_event_t event arrives
		this.configureRequestHandler.handle(this.xcb_generic_event);

		//then
		//the xcb_configure_notify_event_t is posted on the x event bus
		//the event is converted to a GeometryRequest

		verify(this.xEventChannel).post(isA(xcb_configure_request_event_t.class));
	}
}
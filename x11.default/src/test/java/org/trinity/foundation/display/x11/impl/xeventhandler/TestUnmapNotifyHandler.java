package org.trinity.foundation.display.x11.impl.xeventhandler;

import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_unmap_notify_event_t;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.display.x11.impl.XEventChannel;
import org.trinity.foundation.display.x11.impl.XSurfacePool;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LibXcbJNI.class)
public class TestUnmapNotifyHandler {
	@Mock
	private XSurfacePool        xSurfacePool;
	@Mock
	private XEventChannel       xEventChannel;
	@InjectMocks
	private UnmapNotifyHandler  unmapNotifyHandler;
	@Mock
	private xcb_generic_event_t xcb_generic_event;

	private final int childWindowId  = 321;
	private final int targetWindowId = 123;

	@Test
	public void testEventHandling() {
		//given
		//a UnmapNotifyHandler
		//an xcb_generic_event_t

		//when
		//an xcb_generic_event_t event arrives
		this.unmapNotifyHandler.handle(this.xcb_generic_event);

		//then
		//the xcb_unmap_notify_event_t is posted on the x event bus
		//the event is converted to a HideNotify
		verify(this.xEventChannel).post(isA(xcb_unmap_notify_event_t.class));
	}
}

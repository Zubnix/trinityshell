package org.trinity.foundation.display.x11.impl.xeventhandler;

import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.xcb_enter_notify_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
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
public class TestLeaveNotifyHandler {
	@Mock
	private XEventChannel xEventChannel;
	private XSurfacePool  xSurfacePool;
	@InjectMocks
	private LeaveNotifyHandler  leaveNotifyHandler;
	@Mock
	private xcb_generic_event_t xcb_generic_event;

	private final int targetWindowId = 123;

	@Test
	public void testEventHandling() {
		//given
		//a LeaveNotifyHandler
		//an xcb_generic_event_t

		//when
		//an xcb_generic_event_t event arrives
		this.leaveNotifyHandler.handle(this.xcb_generic_event);

		//then
		//the xcb_enter_notify_event_t is posted on the x event bus (enter and leave are the same type in xcb)
		//the event is converted to a PointerLeaveNotify
		verify(this.xEventChannel).post(isA(xcb_enter_notify_event_t.class));
	}
}

package org.trinity.foundation.display.x11.impl.event;

import com.google.common.base.Optional;
import com.google.common.eventbus.EventBus;
import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.LibXcbJNI;
import org.freedesktop.xcb.SWIGTYPE_p_xcb_connection_t;
import org.freedesktop.xcb.xcb_configure_request_event_t;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.display.api.Compositor;
import org.trinity.display.api.event.GeometryRequest;
import org.trinity.foundation.display.x11.impl.DisplaySurfacePool;
import org.trinity.foundation.display.x11.impl.XEventChannel;

import static org.freedesktop.xcb.LibXcbJNI.xcb_configure_request_event_t_border_width_get;
import static org.freedesktop.xcb.LibXcbJNI.xcb_configure_request_event_t_height_get;
import static org.freedesktop.xcb.LibXcbJNI.xcb_configure_request_event_t_value_mask_get;
import static org.freedesktop.xcb.LibXcbJNI.xcb_configure_request_event_t_width_get;
import static org.freedesktop.xcb.LibXcbJNI.xcb_configure_request_event_t_x_get;
import static org.freedesktop.xcb.LibXcbJNI.xcb_configure_request_event_t_y_get;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_WIDTH;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_X;
import static org.freedesktop.xcb.xcb_config_window_t.XCB_CONFIG_WINDOW_Y;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LibXcb.class,
                 LibXcbJNI.class})
public class TestConfigureRequestHandler {

	@Mock
	private EventBus                xEventBus;
	@Mock
	private XEventChannel           xEventChannel;
	@Mock
	private DisplaySurfacePool      xWindowPool;
	@Mock
	private Compositor compositor;
	@InjectMocks
	private ConfigureRequestHandler configureRequestHandler;

	@Mock
	private xcb_generic_event_t         xcb_generic_event;
	@Mock
	private SWIGTYPE_p_xcb_connection_t xcb_connection;

	@Before
	public void setup() {
		when(this.xEventChannel.getXcbConnection()).thenReturn(this.xcb_connection);
	}

	@Test
	public void testEventHandling() {
		//given
		//a ConfigureRequestHandler
		//an xcb_generic_event_t
		mockStatic(LibXcbJNI.class);
		mockStatic(LibXcb.class);

		final Short x = 5;
		final Short y = 10;
		final Integer width = 15;
		final Integer height = 30;
		final Integer border = 3;

		final int valueMask = XCB_CONFIG_WINDOW_X | XCB_CONFIG_WINDOW_Y | XCB_CONFIG_WINDOW_WIDTH;

		when(xcb_configure_request_event_t_x_get(anyLong(),
												 (xcb_configure_request_event_t) any())).thenReturn(x);
		when(xcb_configure_request_event_t_y_get(anyLong(),
												 (xcb_configure_request_event_t) any())).thenReturn(y);
		when(xcb_configure_request_event_t_width_get(anyLong(),
													 (xcb_configure_request_event_t) any())).thenReturn(width);
		when(xcb_configure_request_event_t_height_get(anyLong(),
													  (xcb_configure_request_event_t) any())).thenReturn(height);
		when(xcb_configure_request_event_t_border_width_get(anyLong(),
															(xcb_configure_request_event_t) any())).thenReturn(border);
		when(xcb_configure_request_event_t_value_mask_get(anyLong(),
														  (xcb_configure_request_event_t) any())).thenReturn(valueMask);

		//when
		//an xcb_generic_event_t event arrives
		final Optional<GeometryRequest> geometryRequestOptional = this.configureRequestHandler.handle(this.xcb_generic_event);

		//then
		//the xcb_configure_notify_event_t is posted on the x event bus
		//the event is converted to a GeometryRequest

		verify(this.xEventBus).post(isA(xcb_configure_request_event_t.class));
		assertTrue(geometryRequestOptional.isPresent());
		assertEquals((int) x,
					 geometryRequestOptional.get().getGeometry().getX());
		assertEquals((int) y,
					 geometryRequestOptional.get().getGeometry().getY());
		assertEquals((int) (width + 2 * border),
					 geometryRequestOptional.get().getGeometry().getWidth());
		assertEquals((int) (height + 2 * border),
					 geometryRequestOptional.get().getGeometry().getHeight());
		assertTrue(geometryRequestOptional.get().configureX());
        assertTrue(geometryRequestOptional.get().configureY());
        assertTrue(geometryRequestOptional.get().configureWidth());
        assertFalse(geometryRequestOptional.get().configureHeight());
    }
}

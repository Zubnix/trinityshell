package org.trinity.shellplugin.wm.x11.impl.protocol;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.trinity.shellplugin.wm.x11.impl.XEventHandling;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class XPropertyChangedEventHandling implements XEventHandling {

	private static final Integer eventCode = Integer.valueOf(LibXcb.XCB_PROPERTY_NOTIFY);

	private final XWindowProcol xWindowProcol;

	@Inject
	XPropertyChangedEventHandling(final XWindowProcol xWindowProcol) {
		this.xWindowProcol = xWindowProcol;
	}

	@Override
	public void handle(final xcb_generic_event_t xEvent) {
		final xcb_property_notify_event_t property_notify_event_t = new xcb_property_notify_event_t(xcb_generic_event_t.getCPtr(xEvent),
																									true);
		final int clientId = property_notify_event_t.getWindow();
		this.xWindowProcol.findXWindow(clientId).get().post(property_notify_event_t);
	}

	@Override
	public Integer getEventCode() {
		return eventCode;
	}
}
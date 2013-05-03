package org.trinity.shellplugin.wm.x11.impl.protocol;

import org.freedesktop.xcb.LibXcb;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.shellplugin.wm.x11.impl.XEventHandling;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class XPropertyChangedEventHandling implements XEventHandling {

	private static final Integer eventCode = Integer.valueOf(LibXcb.XCB_PROPERTY_NOTIFY);

	private final XWindowProtocol xWindowProtocol;

	@Inject
	XPropertyChangedEventHandling(final XWindowProtocol xWindowProtocol) {
		this.xWindowProtocol = xWindowProtocol;
	}

	@Override
	public void handle(final xcb_generic_event_t xEvent) {
		final xcb_property_notify_event_t property_notify_event_t = new xcb_property_notify_event_t(xcb_generic_event_t.getCPtr(xEvent),
																									true);
		final int clientId = property_notify_event_t.getWindow();
		final Optional<DisplaySurface> xWindow = this.xWindowProtocol.findXWindow(clientId);
		if (xWindow.isPresent()) {
			xWindow.get().post(property_notify_event_t);
		}
	}

	@Override
	public Integer getEventCode() {
		return eventCode;
	}
}
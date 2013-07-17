package org.trinity.shellplugin.wm.x11.impl.protocol;

import static org.freedesktop.xcb.LibXcbConstants.XCB_PROPERTY_NOTIFY;

import org.apache.onami.autobind.annotations.Bind;
import org.freedesktop.xcb.xcb_generic_event_t;
import org.freedesktop.xcb.xcb_property_notify_event_t;
import org.trinity.foundation.api.display.DisplaySurface;
import org.trinity.foundation.api.display.bindkey.DisplayExecutor;
import org.trinity.foundation.api.shared.ExecutionContext;

import javax.inject.Inject;
import javax.inject.Singleton;


@Bind(multiple = true)
@Singleton
@ExecutionContext(DisplayExecutor.class)
public class XPropertyChangedEventHandling implements XEventHandling {

	private static final Integer eventCode = XCB_PROPERTY_NOTIFY;
	private final XWindowProtocol xWindowProtocol;

	@Inject
	XPropertyChangedEventHandling(final XWindowProtocol xWindowProtocol) {
		this.xWindowProtocol = xWindowProtocol;
	}

	@Override
	public void handle(final xcb_generic_event_t xEvent) {
		final xcb_property_notify_event_t property_notify_event_t = new xcb_property_notify_event_t(xcb_generic_event_t.getCPtr(xEvent),
																									false);
		final int clientId = property_notify_event_t.getWindow();
		final DisplaySurface xWindow = this.xWindowProtocol.findXWindow(clientId);
		xWindow.post(property_notify_event_t);
	}

	@Override
	public Integer getEventCode() {
		return eventCode;
	}
}

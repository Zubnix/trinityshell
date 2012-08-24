package org.trinity.display.x11.core.impl.event;

import java.util.List;

import org.trinity.display.x11.core.impl.XAtomCache;
import org.trinity.display.x11.core.impl.XEventConversion;
import org.trinity.display.x11.core.impl.XPropertyDisplayProtocolMapping;
import org.trinity.display.x11.core.impl.XWindow;
import org.trinity.display.x11.core.impl.XWindowCache;
import org.trinity.foundation.display.api.DisplayProtocol;
import org.trinity.foundation.display.api.event.DisplayEvent;
import org.trinity.foundation.display.api.event.ProtocolNotifyEvent;

import xcbjb.LibXcb;
import xcbjb.xcb_generic_event_t;
import xcbjb.xcb_property_notify_event_t;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;

@Bind(multiple = true)
@Singleton
public class PropertyNotifyConversion implements XEventConversion {

	private final Integer eventCode = Integer.valueOf(LibXcb.XCB_PROPERTY_NOTIFY);

	private final EventBus xEventBus;
	private final XWindowCache xWindowCache;
	private final XAtomCache xAtomCache;
	private final XPropertyDisplayProtocolMapping xPropertyMapping;

	@Inject
	PropertyNotifyConversion(	final EventBus xEventBus,
								final XWindowCache xWindowCache,
								final XAtomCache xAtomCache,
								final XPropertyDisplayProtocolMapping xPropertyMapping) {
		this.xEventBus = xEventBus;
		this.xWindowCache = xWindowCache;
		this.xAtomCache = xAtomCache;
		this.xPropertyMapping = xPropertyMapping;
	}

	@Override
	public DisplayEvent convert(final xcb_generic_event_t event_t) {

		final xcb_property_notify_event_t property_notify_event_t = new xcb_property_notify_event_t(xcb_generic_event_t.getCPtr(event_t),
																									true);
		this.xEventBus.post(property_notify_event_t);

		final int windowId = property_notify_event_t.getWindow();
		final XWindow displayEventSource = this.xWindowCache.getWindow(windowId);

		final int atomId = property_notify_event_t.getAtom();
		final String xProperty = this.xAtomCache.getAtom(atomId);

		final List<DisplayProtocol> displayProtocols = this.xPropertyMapping.toDisplayProtocols(xProperty);

		final DisplayEvent displayEvent = new ProtocolNotifyEvent(	displayEventSource,
																	displayProtocols);

		return displayEvent;
	}

	@Override
	public Integer getEventCode() {

		return this.eventCode;
	}

}

package org.trinity.shellplugin.wm.x11.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.freedesktop.xcb.xcb_generic_event_t;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Singleton
@Bind(to = @To(value = Type.IMPLEMENTATION))
public class XEventHandler {

	private final Map<Short, XEventHandling> xEventHandlingByCode = new HashMap<Short, XEventHandling>();

	@Inject
	XEventHandler(final Set<XEventHandling> xEventHandlings) {
		for (final XEventHandling xEventHandling : xEventHandlings) {
			this.xEventHandlingByCode.put(	xEventHandling.getEventCode(),
											xEventHandling);
		}
	}

	public void handleXEvent(final xcb_generic_event_t xEvent) {
		final short eventCode = xEvent.getResponse_type();

		this.xEventHandlingByCode.get(Short.valueOf(eventCode)).handle(xEvent);
	}
}
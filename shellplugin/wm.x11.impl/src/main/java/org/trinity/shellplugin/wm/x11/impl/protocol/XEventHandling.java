package org.trinity.shellplugin.wm.x11.impl.protocol;

import org.freedesktop.xcb.xcb_generic_event_t;

public interface XEventHandling {
	void handle(xcb_generic_event_t xEvent);

	Integer getEventCode();
}

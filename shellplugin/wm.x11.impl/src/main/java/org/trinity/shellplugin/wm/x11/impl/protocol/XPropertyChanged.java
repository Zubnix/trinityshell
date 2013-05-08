package org.trinity.shellplugin.wm.x11.impl.protocol;

import org.freedesktop.xcb.xcb_property_notify_event_t;

import com.google.common.eventbus.Subscribe;

public interface XPropertyChanged {

	@Subscribe
	void onXPropertyChanged(xcb_property_notify_event_t property_notify_event);
}

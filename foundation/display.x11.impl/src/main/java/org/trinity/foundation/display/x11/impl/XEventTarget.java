package org.trinity.foundation.display.x11.impl;

import org.trinity.foundation.api.display.event.DisplayEvent;

public interface XEventTarget {
	void post(DisplayEvent displayEvent);

	void addListener(Object listener);

	void removeListener(Object listener);
}
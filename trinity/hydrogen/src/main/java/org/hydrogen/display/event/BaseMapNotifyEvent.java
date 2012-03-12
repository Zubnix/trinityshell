package org.hydrogen.display.event;

import org.hydrogen.api.display.event.DisplayEventSource;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.MapNotifyEvent;

public class BaseMapNotifyEvent extends BaseDisplayEvent implements
		MapNotifyEvent {

	public BaseMapNotifyEvent(final DisplayEventSource eventSource) {
		super(DisplayEventType.MAP_NOTIFY, eventSource);
	}

}

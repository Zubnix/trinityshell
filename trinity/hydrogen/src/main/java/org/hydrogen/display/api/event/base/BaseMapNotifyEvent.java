package org.hydrogen.display.api.event.base;

import org.hydrogen.display.api.event.DisplayEventSource;
import org.hydrogen.display.api.event.DisplayEventType;
import org.hydrogen.display.api.event.MapNotifyEvent;

import com.google.inject.Inject;

public class BaseMapNotifyEvent extends BaseDisplayEvent implements
		MapNotifyEvent {

	@Inject
	protected BaseMapNotifyEvent(final DisplayEventSource eventSource) {
		super(DisplayEventType.MAP_NOTIFY, eventSource);
	}

}

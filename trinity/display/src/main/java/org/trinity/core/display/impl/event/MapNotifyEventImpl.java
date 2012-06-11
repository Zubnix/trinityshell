package org.trinity.core.display.impl.event;

import org.trinity.core.display.api.event.DisplayEventSource;
import org.trinity.core.display.api.event.DisplayEventType;
import org.trinity.core.display.api.event.MapNotifyEvent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

public class MapNotifyEventImpl extends DisplayEventImpl implements
		MapNotifyEvent {

	@Inject
	protected MapNotifyEventImpl(	@Named("MapNotify") final DisplayEventType displayEventType,
									@Assisted final DisplayEventSource eventSource) {
		super(displayEventType, eventSource);
	}

}

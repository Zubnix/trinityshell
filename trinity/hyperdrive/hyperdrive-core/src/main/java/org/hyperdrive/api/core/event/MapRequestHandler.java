package org.hyperdrive.api.core.event;

import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.MapRequestEvent;

public abstract class MapRequestHandler implements
		DisplayEventHandler<MapRequestEvent> {

	@Override
	public DisplayEventType getType() {
		return DisplayEventType.MAP_REQUEST;
	}
}

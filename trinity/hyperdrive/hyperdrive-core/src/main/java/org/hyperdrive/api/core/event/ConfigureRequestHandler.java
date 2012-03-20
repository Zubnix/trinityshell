package org.hyperdrive.api.core.event;

import org.hydrogen.api.display.event.ConfigureRequestEvent;
import org.hydrogen.api.display.event.DisplayEventType;

public abstract class ConfigureRequestHandler implements
		DisplayEventHandler<ConfigureRequestEvent> {

	@Override
	public DisplayEventType getType() {
		return DisplayEventType.CONFIGURE_REQUEST;
	}
}

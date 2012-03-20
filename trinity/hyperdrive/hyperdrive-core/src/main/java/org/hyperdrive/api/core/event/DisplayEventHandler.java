package org.hyperdrive.api.core.event;

import org.hydrogen.api.display.event.DisplayEvent;
import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.event.TypeBoundEventHandler;

public interface DisplayEventHandler<E extends DisplayEvent> extends
		TypeBoundEventHandler<DisplayEventType, E> {
}

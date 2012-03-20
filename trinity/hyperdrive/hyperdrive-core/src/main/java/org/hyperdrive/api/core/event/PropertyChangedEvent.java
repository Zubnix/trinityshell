package org.hyperdrive.api.core.event;

import org.hydrogen.api.display.Property;
import org.hydrogen.api.display.PropertyInstance;
import org.hydrogen.api.event.Event;
import org.hyperdrive.api.core.RenderArea;

public interface PropertyChangedEvent<T extends Property<? extends PropertyInstance>>
		extends Event<PropertyChangedEventType> {
	RenderArea getRenderArea();

	T getProperty();

	boolean isPropertyDeleted();
}

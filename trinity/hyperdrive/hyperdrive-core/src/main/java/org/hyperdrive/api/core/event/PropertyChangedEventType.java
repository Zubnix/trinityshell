package org.hyperdrive.api.core.event;

import org.hydrogen.api.event.Type;

public interface PropertyChangedEventType extends Type {
	String getPropertyName();
}

package org.hyperdrive.api.core.event;

import org.hydrogen.api.display.event.DisplayEventType;
import org.hydrogen.api.display.event.KeyNotifyEvent;

public abstract class KeyboardKeyPressedHandler implements
		DisplayEventHandler<KeyNotifyEvent> {

	@Override
	public DisplayEventType getType() {
		return DisplayEventType.KEY_PRESSED;
	}

}

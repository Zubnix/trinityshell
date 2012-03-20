package org.hyperdrive.api.core.event;

import org.hydrogen.api.display.event.ButtonNotifyEvent;
import org.hydrogen.api.display.event.DisplayEventType;

public abstract class MouseButtonPressedHandler implements
		DisplayEventHandler<ButtonNotifyEvent> {

	@Override
	public DisplayEventType getType() {
		return DisplayEventType.BUTTON_PRESSED;
	}

}

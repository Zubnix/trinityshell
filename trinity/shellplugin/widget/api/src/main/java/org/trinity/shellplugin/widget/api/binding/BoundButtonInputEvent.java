package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.input.api.PointerInput;

public class BoundButtonInputEvent extends ButtonNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	public BoundButtonInputEvent(DisplayEventSource displayEventSource, PointerInput pointerInput, String inputSlotName) {
		super(	displayEventSource,
				pointerInput);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return inputSlotName;
	}
}
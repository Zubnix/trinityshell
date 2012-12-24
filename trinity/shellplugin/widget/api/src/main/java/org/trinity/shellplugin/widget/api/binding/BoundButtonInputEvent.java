package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.display.api.event.ButtonNotifyEvent;
import org.trinity.foundation.display.api.event.DisplayEventTarget;
import org.trinity.foundation.input.api.PointerInput;

public class BoundButtonInputEvent extends ButtonNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	public BoundButtonInputEvent(DisplayEventTarget displayEventTarget, PointerInput pointerInput, String inputSlotName) {
		super(	displayEventTarget,
				pointerInput);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return inputSlotName;
	}
}
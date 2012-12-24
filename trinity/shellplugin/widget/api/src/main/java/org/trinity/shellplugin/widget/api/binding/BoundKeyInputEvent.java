package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.display.api.event.DisplayEventTarget;
import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.KeyboardInput;

public class BoundKeyInputEvent extends KeyNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	public BoundKeyInputEvent(DisplayEventTarget displayEventTarget, KeyboardInput input, String inputSlotName) {
		super(	displayEventTarget,
				input);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return inputSlotName;
	}
}
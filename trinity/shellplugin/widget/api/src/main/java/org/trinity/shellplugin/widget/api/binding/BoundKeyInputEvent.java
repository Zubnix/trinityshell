package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.display.api.event.DisplayEventSource;
import org.trinity.foundation.display.api.event.KeyNotifyEvent;
import org.trinity.foundation.input.api.KeyboardInput;

public class BoundKeyInputEvent extends KeyNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	public BoundKeyInputEvent(DisplayEventSource displayEventSource, KeyboardInput input, String inputSlotName) {
		super(	displayEventSource,
				input);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return inputSlotName;
	}
}
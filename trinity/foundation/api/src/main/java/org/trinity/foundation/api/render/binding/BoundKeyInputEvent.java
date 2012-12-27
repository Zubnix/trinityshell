package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.event.DisplayEventTarget;
import org.trinity.foundation.api.display.event.KeyNotifyEvent;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.render.binding.BoundInputEvent;

public class BoundKeyInputEvent extends KeyNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	public BoundKeyInputEvent(	final DisplayEventTarget displayEventTarget,
								final KeyboardInput input,
								final String inputSlotName) {
		super(	displayEventTarget,
				input);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return this.inputSlotName;
	}
}
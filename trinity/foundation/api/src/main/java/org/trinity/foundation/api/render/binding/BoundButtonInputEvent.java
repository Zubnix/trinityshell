package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.event.ButtonNotifyEvent;
import org.trinity.foundation.api.display.event.DisplayEventTarget;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.BoundInputEvent;

public class BoundButtonInputEvent extends ButtonNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	public BoundButtonInputEvent(	final DisplayEventTarget displayEventTarget,
									final PointerInput pointerInput,
									final String inputSlotName) {
		super(	displayEventTarget,
				pointerInput);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return this.inputSlotName;
	}
}
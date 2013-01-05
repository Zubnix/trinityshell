package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.display.event.ButtonNotifyEvent;
import org.trinity.foundation.api.display.input.PointerInput;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class BoundButtonInputEvent extends ButtonNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	@AssistedInject
	BoundButtonInputEvent(	@Assisted final Object displayEventTarget,
							@Assisted final PointerInput pointerInput,
							@Assisted final String inputSlotName) {
		super(	displayEventTarget,
				pointerInput);
		this.inputSlotName = inputSlotName;

	}

	@Override
	public String getInputSlotName() {
		return this.inputSlotName;
	}
}
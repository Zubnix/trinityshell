package org.trinity.foundation.api.render.binding.view;

import org.trinity.foundation.api.display.event.KeyNotifyEvent;
import org.trinity.foundation.api.display.input.KeyboardInput;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class BoundKeyInputEvent extends KeyNotifyEvent implements BoundInputEvent {

	private final String inputSlotName;

	@AssistedInject
	BoundKeyInputEvent(	@Assisted final Object inputTarget,
						@Assisted final KeyboardInput input,
						@Assisted final String inputSlotName) {
		super(	inputTarget,
				input);
		this.inputSlotName = inputSlotName;
	}

	@Override
	public String getInputSlotName() {
		return this.inputSlotName;
	}
}
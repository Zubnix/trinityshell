package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.refactor.model.InputSlot;

public class SubModel {

	private final SubModel subModel = new SubModel();

	@InputSlot
	public void onClick(final PointerInput input) {

	}

	@InputSlot
	public void onKey(final KeyboardInput keyboardInput) {

	}

	public SubModel getSubModel() {
		return this.subModel;
	}
}

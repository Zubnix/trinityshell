package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.model.InputSlot;

public class DummySubModel {

	private final DummySubSubModel dummySubSubModel = new DummySubSubModel();

	@InputSlot
	public void onClick(final PointerInput input) {

	}

	@InputSlot
	public void onKey(final KeyboardInput keyboardInput) {

	}

	public DummySubSubModel getSubSubModel() {
		return this.dummySubSubModel;
	}
}

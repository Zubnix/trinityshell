package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.InputEmitter;
import org.trinity.foundation.api.render.binding.InputSignal;
import org.trinity.foundation.api.render.binding.ViewPropertySlot;

public class DummyView {

	private Object keyEventEmitter = new Object();

	@InputEmitter({ @InputSignal(inputSlotName = "onViewKey", inputType = KeyboardInput.class),
			@InputSignal(inputSlotName = "onClick", inputType = PointerInput.class) })
	public Object inputEventEmitter() {
		return this;
	}

	@InputEmitter(@InputSignal(inputSlotName = "onKey", inputType = KeyboardInput.class))
	public Object keyEventEmitter() {
		return this.keyEventEmitter;
	}

	@ViewPropertySlot({ "object", "nameless" })
	public void viewSlot0(final Object arg) {

	}

	@ViewPropertySlot("primitiveBoolean")
	public void viewSlotPrimitiveBoolean(final boolean arg) {

	}

	public Object getKeyEventEmitter() {
		return this.keyEventEmitter;
	}

	public void setKeyEventEmitter(final Object keyEventEmitter) {
		this.keyEventEmitter = keyEventEmitter;
	}
}

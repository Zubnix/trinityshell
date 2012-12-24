package org.trinity.shellplugin.widget.api.binding;

import org.trinity.foundation.input.api.KeyboardInput;
import org.trinity.foundation.input.api.PointerInput;

public class DummyView {

	private Object keyEventEmitter = new Object();

	@InputEmitter({ @InputSignal(inputSlotName = "onViewKey", inputType = KeyboardInput.class),
			@InputSignal(inputSlotName = "onClick", inputType = PointerInput.class) })
	public Object inputEventEmitter() {
		return this;
	}

	@InputEmitter(@InputSignal(inputSlotName = "onKey", inputType = KeyboardInput.class))
	public Object keyEventEmitter() {
		return keyEventEmitter;
	}

	@ViewPropertySlot({ "object", "nameless" })
	public void viewSlot0(Object arg) {

	}

	@ViewPropertySlot("primitiveBoolean")
	public void viewSlotPrimitiveBoolean(boolean arg) {

	}

	public Object getKeyEventEmitter() {
		return keyEventEmitter;
	}

	public void setKeyEventEmitter(Object keyEventEmitter) {
		this.keyEventEmitter = keyEventEmitter;
	}
}

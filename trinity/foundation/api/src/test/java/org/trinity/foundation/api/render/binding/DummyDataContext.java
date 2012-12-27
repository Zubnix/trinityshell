package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.InputModifier;
import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.display.input.PointerInput;

public class DummyDataContext {

	// private final DummyView view = new DummyView();
	//
	// private Object object;
	// private final Object nameless = "namelessValue";
	// private final boolean primitiveBoolean = true;

	private final DummyDataContext mockedDummyShellWidget;

	public DummyDataContext(final DummyDataContext mockedDummyShellWidget) {
		this.mockedDummyShellWidget = mockedDummyShellWidget;
	}

	@InputSlot(name = "onClick", momentum = { Momentum.STARTED, Momentum.STOPPED }, modifier = { InputModifier.MOD_1,
			InputModifier.MOD_CTRL })
	public void onClick(final PointerInput pointerInput) {
		this.mockedDummyShellWidget.onClick(pointerInput);
	}

	@InputSlot(name = "onKey", momentum = Momentum.STARTED)
	public void onKey(final KeyboardInput keyboardInput) {
		this.mockedDummyShellWidget.onKey(keyboardInput);

	}

	@InputSlot(input = KeyboardInput.class)
	public void onAnyKeyInput(final Input input) {
		this.mockedDummyShellWidget.onAnyKeyInput(input);
	}

	@InputSlot
	public void onAny() {
		this.mockedDummyShellWidget.onAny();
	}

	@View
	public DummyView getView() {
		return this.mockedDummyShellWidget.getView();
	}

	@ViewPropertyChanged("object")
	public void setObject(final Object object) {
		this.mockedDummyShellWidget.setObject(object);

	}

	@ViewProperty("object")
	public Object getObject() {
		return this.mockedDummyShellWidget.getObject();
	}

	@ViewPropertyChanged("nameless")
	public void setNameless(final Object nameless) {
		this.mockedDummyShellWidget.setNameless(nameless);
	}

	@ViewProperty
	public Object getNameless() {
		return this.mockedDummyShellWidget.getNameless();
	}

	@ViewPropertyChanged("primitiveBoolean")
	public void setPrimitiveBoolean(final boolean primitiveBoolean) {
		this.mockedDummyShellWidget.setPrimitiveBoolean(primitiveBoolean);
	}

	@ViewProperty("primitiveBoolean")
	public boolean isPrimitiveBoolean() {
		return this.mockedDummyShellWidget.isPrimitiveBoolean();
	}
}
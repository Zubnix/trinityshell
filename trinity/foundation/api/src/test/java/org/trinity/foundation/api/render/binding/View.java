package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.render.binding.refactor.model.SubModel;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignal;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;
import org.trinity.foundation.api.render.binding.refactor.view.SubViewChanged;

public class View {

	private SubView mouseInputSubView = new SubView();
	private SubView keyInputSubView = new SubView();

	public SubView getInputSubView() {
		return this.keyInputSubView;
	}

	@SubModel("otherSubModel")
	@PropertySlot(propertyName = "booleanProperty", methodName = "handleStringProperty", argumentTypes = String.class, adapter = BooleanToStringAdapter.class)
	public SubView getMouseInputSubView() {
		return this.mouseInputSubView;
	}

	@SubViewChanged("mouseInputSubView")
	public void setSubView(final SubView subView) {
		this.mouseInputSubView = subView;
	}

	@InputSignals(@InputSignal(name = "onKey", inputType = KeyboardInput.class))
	public SubView getKeyInputSubView() {
		return this.keyInputSubView;
	}

	@SubViewChanged("keyInputSubView")
	public void setKeyInputSubView(final SubView keyInputSubView) {
		this.keyInputSubView = keyInputSubView;
	}
}

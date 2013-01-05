package org.trinity.foundation.api.render.binding;

import java.util.ArrayList;
import java.util.List;

import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.render.binding.view.InputSignal;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.SubModel;
import org.trinity.foundation.api.render.binding.view.SubViewChanged;

public class View {

	@SubModel("otherSubModel.subModel")
	@PropertySlots(@PropertySlot(propertyName = "booleanProperty", methodName = "handleStringProperty", argumentTypes = String.class, adapter = BooleanToStringAdapter.class))
	private SubView mouseInputSubView = new SubView();

	@InputSignals(@InputSignal(name = "onKey", inputType = KeyboardInput.class))
	private SubView keyInputSubView = new SubView();

	@SubModel("subModels")
	@ObservableCollection(SubView.class)
	private final List<Object> subViews = new ArrayList<Object>();

	public SubView getInputSubView() {
		return this.keyInputSubView;
	}

	public SubView getMouseInputSubView() {
		return this.mouseInputSubView;
	}

	@SubViewChanged("mouseInputSubView")
	public void setSubView(final SubView subView) {
		this.mouseInputSubView = subView;
	}

	public SubView getKeyInputSubView() {
		return this.keyInputSubView;
	}

	@SubViewChanged("keyInputSubView")
	public void setKeyInputSubView(final SubView keyInputSubView) {
		this.keyInputSubView = keyInputSubView;
	}

	public List<Object> getSubViews() {
		return this.subViews;
	}
}

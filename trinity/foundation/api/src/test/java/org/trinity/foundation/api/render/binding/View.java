package org.trinity.foundation.api.render.binding;

import java.util.ArrayList;
import java.util.List;

import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignal;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;
import org.trinity.foundation.api.render.binding.refactor.view.SubModel;
import org.trinity.foundation.api.render.binding.refactor.view.SubViewChanged;

public class View {

	private SubView mouseInputSubView = new SubView();
	private SubView keyInputSubView = new SubView();

	private final List<Object> subViews = new ArrayList<Object>();

	public SubView getInputSubView() {
		return this.keyInputSubView;
	}

	@SubModel("otherSubModel.subModel")
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

	@SubModel("subModels")
	@ObservableCollection(SubView.class)
	public List<Object> getSubViews() {
		return this.subViews;
	}
}

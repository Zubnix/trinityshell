package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.display.input.KeyboardInput;
import org.trinity.foundation.api.display.input.PointerInput;
import org.trinity.foundation.api.render.binding.model.InputSlot;
import org.trinity.foundation.api.render.binding.model.PropertyChanged;

public class DummySubModel {

	private DummySubSubModel dummySubSubModel = new DummySubSubModel();

	private boolean booleanProperty;

	@PropertyChanged("booleanProperty")
	public void setBooleanProperty(final boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public boolean isBooleanProperty() {
		return this.booleanProperty;
	}

	@InputSlot
	public void onClick(final PointerInput input) {

	}

	@InputSlot
	public void onKey(final KeyboardInput keyboardInput) {

	}

	@PropertyChanged("dummySubSubModel")
	public void setDummySubSubModel(final DummySubSubModel dummySubSubModel) {
		this.dummySubSubModel = dummySubSubModel;
	}

	public DummySubSubModel getSubSubModel() {
		return this.dummySubSubModel;
	}
}

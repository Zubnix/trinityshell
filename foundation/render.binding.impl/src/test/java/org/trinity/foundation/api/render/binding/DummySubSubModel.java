package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;

public class DummySubSubModel {

	private boolean booleanProperty;

	@PropertyChanged("booleanProperty")
	public void setBooleanProperty(final boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public boolean isBooleanProperty() {
		return this.booleanProperty;
	}
}

package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.shared.Listenable;

public class DummySubSubModel implements Listenable {

	private boolean booleanProperty;

	@PropertyChanged("booleanProperty")
	public void setBooleanProperty(final boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public boolean isBooleanProperty() {
		return this.booleanProperty;
	}

	@Override
	public void addListener(final Object listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(final Object listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(final Object event) {
		// TODO Auto-generated method stub

	}
}

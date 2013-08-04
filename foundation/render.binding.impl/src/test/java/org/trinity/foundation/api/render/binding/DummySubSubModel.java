package org.trinity.foundation.api.render.binding;

import java.util.concurrent.ExecutorService;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.shared.AsyncListenable;

public class DummySubSubModel implements AsyncListenable {

	private boolean booleanProperty;

	public boolean isBooleanProperty() {
		return this.booleanProperty;
	}

	@PropertyChanged(value = "booleanProperty", executor = DummyExecutor.class)
	public void setBooleanProperty(final boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	@Override
	public void register(final Object listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(	final Object listener,
							final ExecutorService executor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(final Object listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(final Object event) {
		// TODO Auto-generated method stub

	}

}

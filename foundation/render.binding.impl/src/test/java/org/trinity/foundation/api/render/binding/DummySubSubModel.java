package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.shared.Listenable;

import com.google.common.util.concurrent.ListenableFuture;

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
	public ListenableFuture<Void> addListener(final Object listener) {
		return null;
		// TODO Auto-generated method stub

	}

	@Override
	public ListenableFuture<Void> removeListener(final Object listener) {
		return null;
		// TODO Auto-generated method stub

	}

	@Override
	public ListenableFuture<Void> post(final Object event) {
		return null;
		// TODO Auto-generated method stub

	}
}

package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.shared.AsyncListenable;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;

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
	public void register(@Nonnull final Object listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void register(	@Nonnull final Object listener,
							@Nonnull final ExecutorService executor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scheduleRegister(@Nonnull final Object listener) {
	}

	@Override
	public void scheduleRegister(	@Nonnull final Object listener,
									@Nonnull final ExecutorService listenerActivationExecutor) {
	}

	@Override
	public void unregister(@Nonnull final Object listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(@Nonnull final Object event) {
		// TODO Auto-generated method stub

	}

}

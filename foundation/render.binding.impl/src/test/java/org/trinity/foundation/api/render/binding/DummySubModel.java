package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.shared.AsyncListenable;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;

public class DummySubModel implements AsyncListenable {

	private DummySubSubModel dummySubSubModel = new DummySubSubModel();

	private boolean booleanProperty;

	@PropertyChanged(value = "booleanProperty", executor = DummyExecutor.class)
	public void setBooleanProperty(final boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public boolean isBooleanProperty() {
		return this.booleanProperty;
	}

	public void onClick() {

	}

	public void onKey() {

	}

	@PropertyChanged(value = "dummySubSubModel", executor = DummyExecutor.class)
	public void setDummySubSubModel(final DummySubSubModel dummySubSubModel) {
		this.dummySubSubModel = dummySubSubModel;
	}

	public DummySubSubModel getSubSubModel() {
		return this.dummySubSubModel;
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
	public void unregister(@Nonnull final Object listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(@Nonnull final Object event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void scheduleRegister(@Nonnull final Object listener) {

	}

	@Override
	public void scheduleRegister(	@Nonnull final Object listener,
									@Nonnull final ExecutorService listenerActivationExecutor) {

	}

}

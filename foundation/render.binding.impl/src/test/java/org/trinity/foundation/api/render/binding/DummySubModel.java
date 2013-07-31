package org.trinity.foundation.api.render.binding;

import java.util.concurrent.ExecutorService;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;
import org.trinity.foundation.api.shared.AsyncListenable;

public class DummySubModel implements AsyncListenable {

	private DummySubSubModel dummySubSubModel = new DummySubSubModel();

	private boolean booleanProperty;

	@PropertyChanged("booleanProperty")
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

	@PropertyChanged("dummySubSubModel")
	public void setDummySubSubModel(final DummySubSubModel dummySubSubModel) {
		this.dummySubSubModel = dummySubSubModel;
	}

	public DummySubSubModel getSubSubModel() {
		return this.dummySubSubModel;
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

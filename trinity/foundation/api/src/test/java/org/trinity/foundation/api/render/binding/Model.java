package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

public class Model {
	private DummySubModel dummySubModel = new DummySubModel();
	private final DummySubModel otherSubModel = new DummySubModel();

	private final EventList<DummySubModel> dummySubModels = new BasicEventList<DummySubModel>();

	private boolean booleanProperty;

	public DummySubModel getSubModel() {
		return this.dummySubModel;
	}

	@PropertyChanged("dummySubModel")
	public void setSubModel(final DummySubModel dummySubModel) {
		this.dummySubModel = dummySubModel;
	}

	public boolean isBooleanProperty() {
		return this.booleanProperty;
	}

	@PropertyChanged("booleanProperty")
	public void setBooleanProperty(final boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public DummySubModel getOtherSubModel() {
		return this.otherSubModel;
	}

	public EventList<DummySubModel> getSubModels() {
		return this.dummySubModels;
	}
}

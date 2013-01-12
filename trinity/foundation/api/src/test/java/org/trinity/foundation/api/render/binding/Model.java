package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.model.PropertyChanged;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

public class Model {
	private DummySubModel dummySubModel = new DummySubModel();
	private final DummySubModel otherSubModel = new DummySubModel();

	private final EventList<DummySubModel> dummySubModels = new BasicEventList<DummySubModel>();

	public Model() {
		this.dummySubModels.add(new DummySubModel());
	}

	public DummySubModel getDummySubModel() {
		return this.dummySubModel;
	}

	@PropertyChanged("dummySubModel")
	public void setDummySubModel(final DummySubModel dummySubModel) {
		this.dummySubModel = dummySubModel;
	}

	public DummySubModel getOtherSubModel() {
		return this.otherSubModel;
	}

	public EventList<DummySubModel> getDummySubModels() {
		return this.dummySubModels;
	}
}

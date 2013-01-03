package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.refactor.model.SubModelChanged;
import org.trinity.foundation.api.render.binding.refactor.model.ViewProperty;
import org.trinity.foundation.api.render.binding.refactor.model.ViewPropertyChanged;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

public class Model {
	private SubModel subModel = new SubModel();
	private final SubModel otherSubModel = new SubModel();

	private final EventList<SubModel> subModels = new BasicEventList<SubModel>();

	private boolean booleanProperty;

	public SubModel getSubModel() {
		return this.subModel;
	}

	@SubModelChanged("subModel")
	public void setSubModel(final SubModel subModel) {
		this.subModel = subModel;
	}

	@ViewProperty
	public boolean isBooleanProperty() {
		return this.booleanProperty;
	}

	@ViewPropertyChanged("booleanProperty")
	public void setBooleanProperty(final boolean booleanProperty) {
		this.booleanProperty = booleanProperty;
	}

	public SubModel getOtherSubModel() {
		return this.otherSubModel;
	}

	public EventList<SubModel> getSubModels() {
		return this.subModels;
	}
}

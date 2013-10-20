package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

@ObservableCollection(value = "dummySubModels", view = CollectionElementView.class)
@PropertySlots(@PropertySlot(dataModelContext = "class", propertyName = "name", methodName = "setClassName", argumentTypes = String.class))
public class View {

	@DataModelContext("otherSubModel.subSubModel")
	@PropertySlots(@PropertySlot(propertyName = "booleanProperty", methodName = "handleStringProperty", argumentTypes = String.class, adapter = BooleanToStringAdapter.class))
	private SubView mouseInputSubView = new SubView();
	@EventSignals(@EventSignal(name = "onKey", filter = EventSignalFilter.class))
	private SubView keyInputSubView = new SubView();
	private String className;

	public SubView getMouseInputSubView() {
		return this.mouseInputSubView;
	}

	public void setSubView(final SubView subView) {
		this.mouseInputSubView = subView;
	}

	public SubView getKeyInputSubView() {
		return this.keyInputSubView;
	}

	public void setKeyInputSubView(final SubView keyInputSubView) {
		this.keyInputSubView = keyInputSubView;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(final String className) {
		this.className = className;
	}
}

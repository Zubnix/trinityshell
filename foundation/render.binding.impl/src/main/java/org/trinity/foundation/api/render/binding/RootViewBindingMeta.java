package org.trinity.foundation.api.render.binding;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlot;

import java.util.LinkedList;

public class RootViewBindingMeta extends ViewBindingMeta {

	private final DataModelProperty rootDataModelProperty;
	private final Object            viewModel;
	private final Object            dataModel;
	private final String            dataContextPath;

	RootViewBindingMeta(final Object viewModel,
						final Object dataModel,
						final Optional<ObservableCollection> observableCollection,
						final Optional<DataModelContext> dataModelContext,
						final Optional<EventSignal[]> eventSignals,
						final Optional<PropertySlot[]> propertySlots) {
		super(viewModel,
			  observableCollection,
			  dataModelContext,
			  eventSignals,
			  propertySlots);
		this.viewModel = viewModel;
		this.dataModel = dataModel;
		this.rootDataModelProperty = new RootDataModelProperty(this.dataModel);
		this.dataContextPath = dataModelContext.isPresent() ? dataModelContext.get().value() : "";
	}

	@Override
	public boolean resolveDataModelChain(final LinkedList<DataModelProperty> dataModelChain) {
		dataModelChain.add(this.rootDataModelProperty);
		return ViewBindingsUtil.appendDataModelPropertyChain(dataModelChain,
															 this.dataContextPath);
	}

	@Override
	public boolean equals(final Object obj) {

		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final RootViewBindingMeta other = (RootViewBindingMeta) obj;

		return Objects.equal(this.dataModel,
							 other.dataModel)
				&& Objects.equal(this.viewModel,
								 other.viewModel);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.viewModel,
								this.dataModel);
	}
}

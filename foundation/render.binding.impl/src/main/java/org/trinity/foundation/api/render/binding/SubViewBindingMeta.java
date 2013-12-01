package org.trinity.foundation.api.render.binding;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlot;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class SubViewBindingMeta extends ViewBindingMeta {

	private final String          dataContextPath;
	private final Object          viewModel;
	private final ViewBindingMeta parentViewBindingMeta;
	private final Field           subViewField;

	SubViewBindingMeta(final Object viewModel,
					   final ViewBindingMeta parentViewBindingMeta,
					   final Field subViewField,
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
		this.parentViewBindingMeta = parentViewBindingMeta;
		this.subViewField = subViewField;
		this.dataContextPath = dataModelContext.isPresent() ? dataModelContext.get().value() : "";
	}

	@Override
	public boolean resolveDataModelChain(final LinkedList<DataModelProperty> dataModelChain) {
		final Boolean parentSuccess = this.parentViewBindingMeta.resolveDataModelChain(dataModelChain);
		Boolean success = Boolean.FALSE;
		if(parentSuccess) {
			success = ViewBindingsUtil.appendDataModelPropertyChain(dataModelChain,
																	this.dataContextPath);
		}
		return parentSuccess && success;
	}

	@Override
	public boolean equals(final Object obj) {
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final SubViewBindingMeta other = (SubViewBindingMeta) obj;

		return Objects.equal(this.viewModel,
							 other.viewModel)
				&& Objects.equal(this.parentViewBindingMeta,
								 other.parentViewBindingMeta)
				&& Objects.equal(this.subViewField,
								 other.subViewField);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.viewModel,
								this.parentViewBindingMeta,
								this.subViewField);
	}
}
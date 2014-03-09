package org.trinity.foundation.api.binding.binding;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import org.trinity.binding.api.view.DataModelContext;
import org.trinity.binding.api.view.EventSignals;
import org.trinity.binding.api.view.ObservableCollection;
import org.trinity.binding.api.view.PropertySlots;

import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ViewBindingMeta {

	public abstract Optional<ObservableCollection> getObservableCollection();
	public abstract Optional<EventSignals> getEventSignals();
	public abstract Optional<PropertySlots> getPropertySlots();
	public abstract Optional<DataModelContext> getDataModelContext();

	public abstract Object getViewModel();

	protected boolean appendDataModelPropertyChain(final LinkedList<DataModelProperty> dataModelPropertyChain,
												   final String propertyChain) {
		checkNotNull(dataModelPropertyChain);
		checkNotNull(propertyChain);
		checkArgument(!dataModelPropertyChain.isEmpty());

		final Iterable<String> propertyNames = toPropertyNames(propertyChain);
		final LinkedList<DataModelProperty> appendedDataModelChain = new LinkedList<>();

		boolean aborted = false;
		DataModelProperty dataModelProperty = dataModelPropertyChain.getLast();

		for(final String propertyName : propertyNames) {

			final Optional<Object> propertyValue = dataModelProperty.getPropertyValue();

			if(propertyValue.isPresent()) {
				final Object nextDataModel = propertyValue.get();
				dataModelProperty = RelativeDataModelProperty.create(nextDataModel,
																  propertyName);
				appendedDataModelChain.add(dataModelProperty);
			}
			else {
				aborted = true;
				break;
			}
		}

		dataModelPropertyChain.addAll(appendedDataModelChain);
		return !aborted;
	}

	protected static Iterable<String> toPropertyNames(final String subModelPath) {
		checkNotNull(subModelPath);

		return Splitter.on('.').trimResults().omitEmptyStrings().split(subModelPath);
	}

	public abstract boolean resolveDataModelChain(final LinkedList<DataModelProperty> dataModelChain);
}

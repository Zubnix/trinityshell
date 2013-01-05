package org.trinity.foundation.api.render.binding;

import org.trinity.foundation.api.render.binding.view.InputSignal;
import org.trinity.foundation.api.render.binding.view.InputSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.SubModel;

import com.google.common.base.Optional;

public class NewBinder {

	public void bind(	final Object model,
						final Object view) {
		final Class<?> modelClass = model.getClass();

		final Optional<InputSignals> optionalInputSignals = Optional.fromNullable(modelClass
				.getAnnotation(InputSignals.class));
		final Optional<ObservableCollection> optionalObservableCollection = Optional.fromNullable(modelClass
				.getAnnotation(ObservableCollection.class));
	}

	protected void bind(final Object model,
						final Object view,
						final Optional<SubModel> optionalSubModel,
						final Optional<InputSignals> optionalInputSignals,
						final Optional<ObservableCollection> optionalObservableCollection,
						final Optional<PropertySlots> optionalPropertySlots) {

		if (optionalSubModel.isPresent()) {
			final String property = optionalSubModel.get().value();
			// TODO change model
		}

		if (optionalInputSignals.isPresent()) {
			final InputSignal[] inputSignals = optionalInputSignals.get().value();
			// TODO install input emitters
		}

		if (optionalObservableCollection.isPresent()) {
			final ObservableCollection observableCollection = optionalObservableCollection.get();
			// TODO link to observableCollection
		}
	}
}

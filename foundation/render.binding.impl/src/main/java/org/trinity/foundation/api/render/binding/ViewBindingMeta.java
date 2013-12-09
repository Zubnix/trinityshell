package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ViewBindingMeta {

	private final Object viewModel;

	private final Optional<ObservableCollection> observableCollection;
	private final Optional<DataModelContext>     dataModelContext;
	private final Optional<EventSignals>        eventSignals;
	private final Optional<PropertySlots>       propertySlots;

	ViewBindingMeta(final Object viewModel,
					final Optional<ObservableCollection> observableCollection,
					final Optional<DataModelContext> dataModelContext,
					final Optional<EventSignals> eventSignals,
					final Optional<PropertySlots> propertySlots) {

		this.viewModel = viewModel;
		this.observableCollection = observableCollection;
		this.dataModelContext = dataModelContext;
		this.eventSignals = eventSignals;
		this.propertySlots = propertySlots;
	}

	public static ViewBindingMeta create(final Object dataModel,
										 final Object viewModel) {

		final Class<?> subviewClass = viewModel.getClass();

		final Optional<ObservableCollection> observableCollection = scanClassObservableCollection(subviewClass);
		final Optional<DataModelContext> dataModelContext = scanClassDataModelContext(subviewClass);
		final Optional<EventSignals> eventSignals = scanClassEventSignals(subviewClass);
		final Optional<PropertySlots> propertySlots = scanClassPropertySlots(subviewClass);

		return new RootViewBindingMeta(viewModel,
									   dataModel,
									   observableCollection,
									   dataModelContext,
									   eventSignals,
									   propertySlots);
	}

	public static ViewBindingMeta create(@Nonnull final ViewBindingMeta parentViewBindingMeta,
										 @Nonnull final Field subviewField,
										 @Nonnull final Object subviewValue) {

		final Class<?> subviewClass = subviewValue.getClass();

		final Optional<ObservableCollection> observableCollection = scanFieldObservableCollection(subviewField).or(scanClassObservableCollection(subviewClass));
		final Optional<DataModelContext> dataModelContext = scanFieldDataModelContext(subviewField).or(scanClassDataModelContext(subviewClass));
		final Optional<EventSignals> eventSignals = scanFieldEventSignals(subviewField).or(scanClassEventSignals(subviewClass));
		final Optional<PropertySlots> propertySlots = scanFieldPropertySlots(subviewField).or(scanClassPropertySlots(subviewClass));

		return new SubViewBindingMeta(subviewValue,
									  parentViewBindingMeta,
									  subviewField,
									  observableCollection,
									  dataModelContext,
									  eventSignals,
									  propertySlots);
	}

	private static Optional<PropertySlots> scanFieldPropertySlots(final Field subviewField) {

		final PropertySlots fieldLevelPropertySlots = subviewField.getAnnotation(PropertySlots.class);
		final Optional<PropertySlots> fieldLevelOptionalPropertySlots;
		if(fieldLevelPropertySlots == null) {
			fieldLevelOptionalPropertySlots = Optional.absent();
		}
		else {
			fieldLevelOptionalPropertySlots = Optional.of(fieldLevelPropertySlots);
		}

		return fieldLevelOptionalPropertySlots;
	}

    private static Optional<PropertySlots> scanClassPropertySlots(final Class<?> subviewClass) {

        final PropertySlots classLevelPropertySlots = subviewClass.getAnnotation(PropertySlots.class);
        final Optional<PropertySlots> classLevelOptionalPropertySlots;
        if(classLevelPropertySlots == null) {
            classLevelOptionalPropertySlots = Optional.absent();
        } else {
            classLevelOptionalPropertySlots = Optional.of(classLevelPropertySlots);
        }

        return classLevelOptionalPropertySlots;
    }

    private static Optional<EventSignals> scanClassEventSignals(final Class<?> subviewClass) {
        final EventSignals classLevelEventSignals = subviewClass.getAnnotation(EventSignals.class);
        final Optional<EventSignals> classLevelOptionalEventSignals;
        if(classLevelEventSignals == null) {
            classLevelOptionalEventSignals = Optional.absent();
        } else {
            classLevelOptionalEventSignals = Optional.of(classLevelEventSignals);
        }

        return classLevelOptionalEventSignals;
    }

    private static Optional<EventSignals> scanFieldEventSignals(final Field subviewField) {
        final EventSignals fieldLevelEventSignals = subviewField.getAnnotation(EventSignals.class);
        final Optional<EventSignals> fieldLevelOptionalEventSignals;
        if(fieldLevelEventSignals == null) {
            fieldLevelOptionalEventSignals = Optional.absent();
        } else {
            fieldLevelOptionalEventSignals = Optional.of(fieldLevelEventSignals);
        }
        return fieldLevelOptionalEventSignals;
    }

    private static Optional<ObservableCollection> scanFieldObservableCollection(final Field subviewField) {
        return Optional.fromNullable(subviewField.getAnnotation(ObservableCollection.class));
    }

    private static Optional<ObservableCollection> scanClassObservableCollection(final Class<?> subviewClass) {
        return Optional.fromNullable(subviewClass.getAnnotation(ObservableCollection.class));
    }

    private static Optional<DataModelContext> scanFieldDataModelContext(final Field subviewField) {
        return Optional.fromNullable(subviewField.getAnnotation(DataModelContext.class));
    }

    private static Optional<DataModelContext> scanClassDataModelContext(final Class<?> subviewClass) {
        return Optional.fromNullable(subviewClass.getAnnotation(DataModelContext.class));
    }


	public Optional<ObservableCollection> getObservableCollection() {
		return this.observableCollection;
	}

	public Optional<EventSignals> getEventSignals() {
		return this.eventSignals;
	}

	public Optional<PropertySlots> getPropertySlots() {
		return this.propertySlots;
	}

	public Optional<DataModelContext> getDataModelContext() {
		return this.dataModelContext;
	}

	public Object getViewModel() {
		return this.viewModel;
    }

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
                dataModelProperty = new DataModelPropertyImpl(nextDataModel,
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

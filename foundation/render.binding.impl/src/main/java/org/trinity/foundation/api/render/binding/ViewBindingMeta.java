package org.trinity.foundation.api.render.binding;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.SubView;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.LinkedList;

public class ViewBindingMeta {

    private final Function<LinkedList<DataModelProperty>, Boolean> dataContextResolution;

    private final Object viewModel;

    private final Optional<ObservableCollection> observableCollection;
    private final Optional<DataModelContext> dataModelContext;
    private final Optional<EventSignal[]> eventSignals;
    private final Optional<PropertySlot[]> propertySlots;

    public static ViewBindingMeta create(final Object dataModel,
                                         final Object viewModel) {

        final Class<?> subviewClass = viewModel.getClass();

        final Optional<ObservableCollection> observableCollection = scanClassObservableCollection(subviewClass);
        final Optional<DataModelContext> dataModelContext = scanClassDataModelContext(subviewClass);
        final Optional<EventSignal[]> eventSignals = scanClassEventSignals(subviewClass);
        final Optional<PropertySlot[]> propertySlots = scanClassPropertySlots(subviewClass);

        return new ViewBindingMeta(viewModel,
                                   dataModel,
                                   observableCollection,
                                   dataModelContext,
                                   eventSignals,
                                   propertySlots);
    }

    public static Optional<ViewBindingMeta> create(final ViewBindingMeta parentViewBindingMeta,
                                                   final Field subviewField) throws IllegalAccessException {

        if(subviewField.getAnnotation(SubView.class) == null) {
            return Optional.absent();
        }

        subviewField.setAccessible(true);
        final Object viewModel = subviewField.get(parentViewBindingMeta.getViewModel());
        final Class<?> subviewClass = viewModel.getClass();

        final Optional<ObservableCollection> observableCollection = scanFieldObservableCollection(subviewField).or(scanClassObservableCollection(subviewClass));
        final Optional<DataModelContext> dataModelContext = scanFieldDataModelContext(subviewField).or(scanClassDataModelContext(subviewClass));
        final Optional<EventSignal[]> eventSignals = scanFieldEventSignals(subviewField).or(scanClassEventSignals(subviewClass));
        final Optional<PropertySlot[]> propertySlots = scanFieldPropertySlots(subviewField).or(scanClassPropertySlots(subviewClass));

        return Optional.of(new ViewBindingMeta(viewModel,
                                               parentViewBindingMeta,
                                               observableCollection,
                                               dataModelContext,
                                               eventSignals,
                                               propertySlots));
    }

    private static Optional<PropertySlot[]> scanFieldPropertySlots(final Field subviewField) {

        final PropertySlots fieldLevelPropertySlots = subviewField.getAnnotation(PropertySlots.class);
        final Optional<PropertySlot[]> fieldLevelOptionalPropertySlots;
        if(fieldLevelPropertySlots == null) {
            fieldLevelOptionalPropertySlots = Optional.absent();
        } else {
            fieldLevelOptionalPropertySlots = Optional.of(fieldLevelPropertySlots.value());
        }

        return fieldLevelOptionalPropertySlots;
    }

    private static Optional<PropertySlot[]> scanClassPropertySlots(final Class<?> subviewClass) {

        final PropertySlots classLevelPropertySlots = subviewClass.getAnnotation(PropertySlots.class);
        final Optional<PropertySlot[]> classLevelOptionalPropertySlots;
        if(classLevelPropertySlots == null) {
            classLevelOptionalPropertySlots = Optional.absent();
        } else {
            classLevelOptionalPropertySlots = Optional.of(classLevelPropertySlots.value());
        }

        return classLevelOptionalPropertySlots;
    }

    private static Optional<EventSignal[]> scanClassEventSignals(final Class<?> subviewClass) {
        final EventSignals classLevelEventSignals = subviewClass.getAnnotation(EventSignals.class);
        final Optional<EventSignal[]> classLevelOptionalEventSignals;
        if(classLevelEventSignals == null) {
            classLevelOptionalEventSignals = Optional.absent();
        } else {
            classLevelOptionalEventSignals = Optional.of(classLevelEventSignals.value());
        }

        return classLevelOptionalEventSignals;
    }

    private static Optional<EventSignal[]> scanFieldEventSignals(final Field subviewField) {
        final EventSignals fieldLevelEventSignals = subviewField.getAnnotation(EventSignals.class);
        final Optional<EventSignal[]> fieldLevelOptionalEventSignals;
        if(fieldLevelEventSignals == null) {
            fieldLevelOptionalEventSignals = Optional.absent();
        } else {
            fieldLevelOptionalEventSignals = Optional.of(fieldLevelEventSignals.value());
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

    private ViewBindingMeta(final Object viewModel,
                            final ViewBindingMeta parentViewBindingMeta,
                            final Optional<ObservableCollection> observableCollection,
                            final Optional<DataModelContext> dataModelContext,
                            final Optional<EventSignal[]> eventSignals,
                            final Optional<PropertySlot[]> propertySlots) {
        this.viewModel = viewModel;
        this.observableCollection = observableCollection;
        this.dataModelContext = dataModelContext;
        this.eventSignals = eventSignals;
        this.propertySlots = propertySlots;

        final String dataContextPath = dataModelContext.isPresent() ? dataModelContext.get().value() : "";

        this.dataContextResolution = new Function<LinkedList<DataModelProperty>, Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable final LinkedList<DataModelProperty> input) {
                final Boolean parentSuccess = parentViewBindingMeta.resolveDataModelChain(input);
                Boolean success = Boolean.FALSE;
                if(parentSuccess) {
                    success = DataContextNavigator.appendDataModelPropertyChain(input,
                                                                                dataContextPath);
                }
                return parentSuccess && success;
            }
        };
    }

    private ViewBindingMeta(final Object viewModel,
                            final Object dataModel,
                            final Optional<ObservableCollection> observableCollection,
                            final Optional<DataModelContext> dataModelContext,
                            final Optional<EventSignal[]> eventSignals,
                            final Optional<PropertySlot[]> propertySlots) {
        this.viewModel = viewModel;
        this.observableCollection = observableCollection;
        this.dataModelContext = dataModelContext;
        this.eventSignals = eventSignals;
        this.propertySlots = propertySlots;

        final RootDataModelProperty rootDataModelProperty = new RootDataModelProperty(dataModel);
        this.dataContextResolution = new Function<LinkedList<DataModelProperty>, Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable final LinkedList<DataModelProperty> input) {
                input.add(rootDataModelProperty);
                return Boolean.TRUE;
            }
        };
    }

    public Optional<ObservableCollection> getObservableCollection() {
        return this.observableCollection;
    }

    public Optional<EventSignal[]> getEventSignals() {
        return this.eventSignals;
    }

    public Optional<PropertySlot[]> getPropertySlots() {
        return this.propertySlots;
    }

    public Optional<DataModelContext> getDataModelContext() {
        return this.dataModelContext;
    }

    public Object getViewModel() {
        return this.viewModel;
    }

    public boolean resolveDataModelChain(final LinkedList<DataModelProperty> dataModelChain) {
        return this.dataContextResolution.apply(dataModelChain);
    }
}

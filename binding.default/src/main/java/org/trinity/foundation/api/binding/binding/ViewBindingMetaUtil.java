package org.trinity.foundation.api.binding.binding;

import com.google.common.base.Optional;
import org.trinity.binding.api.view.DataModelContext;
import org.trinity.binding.api.view.EventSignals;
import org.trinity.binding.api.view.ObservableCollection;
import org.trinity.binding.api.view.PropertySlots;

import java.lang.reflect.Field;

/**
 *
 */
final class ViewBindingMetaUtil {
    ViewBindingMetaUtil() {
    }

    static Optional<PropertySlots> scanFieldPropertySlots(final Field subviewField) {

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

    static Optional<PropertySlots> scanClassPropertySlots(final Class<?> subviewClass) {

        final PropertySlots classLevelPropertySlots = subviewClass.getAnnotation(PropertySlots.class);
        final Optional<PropertySlots> classLevelOptionalPropertySlots;
        if(classLevelPropertySlots == null) {
            classLevelOptionalPropertySlots = Optional.absent();
        }
        else {
            classLevelOptionalPropertySlots = Optional.of(classLevelPropertySlots);
        }

        return classLevelOptionalPropertySlots;
    }

    static Optional<EventSignals> scanClassEventSignals(final Class<?> subviewClass) {
        final EventSignals classLevelEventSignals = subviewClass.getAnnotation(EventSignals.class);
        final Optional<EventSignals> classLevelOptionalEventSignals;
        if(classLevelEventSignals == null) {
            classLevelOptionalEventSignals = Optional.absent();
        }
        else {
            classLevelOptionalEventSignals = Optional.of(classLevelEventSignals);
        }

        return classLevelOptionalEventSignals;
    }

    static Optional<EventSignals> scanFieldEventSignals(final Field subviewField) {
        final EventSignals fieldLevelEventSignals = subviewField.getAnnotation(EventSignals.class);
        final Optional<EventSignals> fieldLevelOptionalEventSignals;
        if(fieldLevelEventSignals == null) {
            fieldLevelOptionalEventSignals = Optional.absent();
        }
        else {
            fieldLevelOptionalEventSignals = Optional.of(fieldLevelEventSignals);
        }
        return fieldLevelOptionalEventSignals;
    }

    static Optional<ObservableCollection> scanFieldObservableCollection(final Field subviewField) {
        return Optional.fromNullable(subviewField.getAnnotation(ObservableCollection.class));
    }

    static Optional<ObservableCollection> scanClassObservableCollection(final Class<?> subviewClass) {
        return Optional.fromNullable(subviewClass.getAnnotation(ObservableCollection.class));
    }

    static Optional<DataModelContext> scanFieldDataModelContext(final Field subviewField) {
        return Optional.fromNullable(subviewField.getAnnotation(DataModelContext.class));
    }

    static Optional<DataModelContext> scanClassDataModelContext(final Class<?> subviewClass) {
        return Optional.fromNullable(subviewClass.getAnnotation(DataModelContext.class));
    }
}

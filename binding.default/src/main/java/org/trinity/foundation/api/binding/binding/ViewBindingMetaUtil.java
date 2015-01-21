package org.trinity.foundation.api.binding.binding;

import com.google.common.base.Optional;
import org.trinity.binding.api.view.DataModelContext;
import org.trinity.binding.api.view.EventSignals;
import org.trinity.binding.api.view.ObservableCollection;
import org.trinity.binding.api.view.PropertySlots;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

/**
 *
 */
final class ViewBindingMetaUtil {
    ViewBindingMetaUtil() {
    }

    @Nonnull
    static Optional<PropertySlots> scanFieldPropertySlots(@Nonnull final Field subviewField) {

        final PropertySlots fieldLevelPropertySlots = subviewField.getAnnotation(PropertySlots.class);
        final Optional<PropertySlots> fieldLevelOptionalPropertySlots;
        if (fieldLevelPropertySlots == null) {
            fieldLevelOptionalPropertySlots = Optional.absent();
        }
        else {
            fieldLevelOptionalPropertySlots = Optional.of(fieldLevelPropertySlots);
        }

        return fieldLevelOptionalPropertySlots;
    }

    @Nonnull
    static Optional<PropertySlots> scanClassPropertySlots(@Nonnull final Class<?> subviewClass) {

        final PropertySlots classLevelPropertySlots = subviewClass.getAnnotation(PropertySlots.class);
        final Optional<PropertySlots> classLevelOptionalPropertySlots;
        if (classLevelPropertySlots == null) {
            classLevelOptionalPropertySlots = Optional.absent();
        }
        else {
            classLevelOptionalPropertySlots = Optional.of(classLevelPropertySlots);
        }

        return classLevelOptionalPropertySlots;
    }

    @Nonnull
    static Optional<EventSignals> scanClassEventSignals(@Nonnull final Class<?> subviewClass) {
        final EventSignals classLevelEventSignals = subviewClass.getAnnotation(EventSignals.class);
        final Optional<EventSignals> classLevelOptionalEventSignals;
        if (classLevelEventSignals == null) {
            classLevelOptionalEventSignals = Optional.absent();
        }
        else {
            classLevelOptionalEventSignals = Optional.of(classLevelEventSignals);
        }

        return classLevelOptionalEventSignals;
    }

    @Nonnull
    static Optional<EventSignals> scanFieldEventSignals(@Nonnull final Field subviewField) {
        final EventSignals fieldLevelEventSignals = subviewField.getAnnotation(EventSignals.class);
        final Optional<EventSignals> fieldLevelOptionalEventSignals;
        if (fieldLevelEventSignals == null) {
            fieldLevelOptionalEventSignals = Optional.absent();
        }
        else {
            fieldLevelOptionalEventSignals = Optional.of(fieldLevelEventSignals);
        }
        return fieldLevelOptionalEventSignals;
    }

    @Nonnull
    static Optional<ObservableCollection> scanFieldObservableCollection(@Nonnull final Field subviewField) {
        return Optional.fromNullable(subviewField.getAnnotation(ObservableCollection.class));
    }

    @Nonnull
    static Optional<ObservableCollection> scanClassObservableCollection(@Nonnull final Class<?> subviewClass) {
        return Optional.fromNullable(subviewClass.getAnnotation(ObservableCollection.class));
    }

    @Nonnull
    static Optional<DataModelContext> scanFieldDataModelContext(@Nonnull final Field subviewField) {
        return Optional.fromNullable(subviewField.getAnnotation(DataModelContext.class));
    }

    @Nonnull
    static Optional<DataModelContext> scanClassDataModelContext(@Nonnull final Class<?> subviewClass) {
        return Optional.fromNullable(subviewClass.getAnnotation(DataModelContext.class));
    }
}

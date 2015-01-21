package org.trinity.foundation.api.binding.binding;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import org.trinity.binding.api.view.DataModelContext;
import org.trinity.binding.api.view.EventSignals;
import org.trinity.binding.api.view.ObservableCollection;
import org.trinity.binding.api.view.PropertySlots;

import javax.annotation.Nonnull;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ViewBindingMeta {

    @Nonnull
    public abstract Optional<ObservableCollection> getObservableCollection();

    @Nonnull
    public abstract Optional<EventSignals> getEventSignals();

    @Nonnull
    public abstract Optional<PropertySlots> getPropertySlots();

    @Nonnull
    public abstract Optional<DataModelContext> getDataModelContext();

    @Nonnull
    public abstract Object getViewModel();

    protected boolean appendDataModelPropertyChain(@Nonnull final LinkedList<DataModelProperty> dataModelPropertyChain,
                                                   @Nonnull final String propertyChain) {
        checkNotNull(dataModelPropertyChain);
        checkNotNull(propertyChain);
        checkArgument(!dataModelPropertyChain.isEmpty());

        final Iterable<String> propertyNames = toPropertyNames(propertyChain);
        final LinkedList<DataModelProperty> appendedDataModelChain = new LinkedList<>();

        boolean aborted = false;
        DataModelProperty dataModelProperty = dataModelPropertyChain.getLast();

        for (final String propertyName : propertyNames) {

            final Optional<Object> propertyValue = dataModelProperty.getPropertyValue();

            if (propertyValue.isPresent()) {
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

    @Nonnull
    protected static Iterable<String> toPropertyNames(@Nonnull final String subModelPath) {
        checkNotNull(subModelPath);

        return Splitter.on('.')
                       .trimResults()
                       .omitEmptyStrings()
                       .split(subModelPath);
    }

    public abstract boolean resolveDataModelChain(@Nonnull final LinkedList<DataModelProperty> dataModelChain);
}

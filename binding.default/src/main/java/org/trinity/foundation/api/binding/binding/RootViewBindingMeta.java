package org.trinity.foundation.api.binding.binding;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import org.trinity.binding.api.view.DataModelContext;
import org.trinity.binding.api.view.EventSignals;
import org.trinity.binding.api.view.ObservableCollection;
import org.trinity.binding.api.view.PropertySlots;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.LinkedList;

@Immutable
@AutoValue
public abstract class RootViewBindingMeta extends ViewBindingMeta {

    private DataModelProperty              rootDataModelProperty;
    private String                         dataContextPath;
    private Optional<ObservableCollection> observableCollection;
    private Optional<DataModelContext>     dataModelContext;
    private Optional<EventSignals>         eventSignals;
    private Optional<PropertySlots>        propertySlots;

    public static ViewBindingMeta create(@Nonnull final Object dataModel,
                                         @Nonnull final Object viewModel) {

        return new AutoValue_RootViewBindingMeta(viewModel,
                                                 dataModel).scan();
    }

    public abstract Object getDataModel();

    ViewBindingMeta scan() {
        final Class<?> subviewClass = getViewModel().getClass();

        this.observableCollection = ViewBindingMetaUtil.scanClassObservableCollection(subviewClass);
        this.dataModelContext = ViewBindingMetaUtil.scanClassDataModelContext(subviewClass);
        this.eventSignals = ViewBindingMetaUtil.scanClassEventSignals(subviewClass);
        this.propertySlots = ViewBindingMetaUtil.scanClassPropertySlots(subviewClass);

        this.rootDataModelProperty = ConstantDataModelProperty.create(getDataModel());
        this.dataContextPath = this.dataModelContext.isPresent() ? this.dataModelContext.get()
                                                                                        .value() : "";

        return this;
    }

    @Nonnull
    @Override
    public Optional<ObservableCollection> getObservableCollection() {
        return this.observableCollection;
    }

    @Nonnull
    @Override
    public Optional<DataModelContext> getDataModelContext() {
        return this.dataModelContext;
    }

    @Nonnull
    @Override
    public Optional<EventSignals> getEventSignals() {
        return this.eventSignals;
    }

    @Nonnull
    @Override
    public Optional<PropertySlots> getPropertySlots() {
        return this.propertySlots;
    }

    @Override
    public boolean resolveDataModelChain(@Nonnull final LinkedList<DataModelProperty> dataModelChain) {
        dataModelChain.add(this.rootDataModelProperty);
        return appendDataModelPropertyChain(dataModelChain,
                                            this.dataContextPath);
    }
}

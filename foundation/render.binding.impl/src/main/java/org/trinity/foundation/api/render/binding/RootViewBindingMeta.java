package org.trinity.foundation.api.render.binding;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

import javax.annotation.concurrent.Immutable;
import java.util.LinkedList;

@Immutable
@AutoValue
public abstract class RootViewBindingMeta extends ViewBindingMeta {

    private DataModelProperty rootDataModelProperty;
	private String            dataContextPath;
    private Optional<ObservableCollection> observableCollection;
    private Optional<DataModelContext> dataModelContext;
    private Optional<EventSignals> eventSignals;
    private Optional<PropertySlots> propertySlots;

    public static ViewBindingMeta create(final Object dataModel,
                                         final Object viewModel) {

        return new AutoValue_RootViewBindingMeta(viewModel, dataModel).scan();
    }

    public abstract Object getDataModel();

    ViewBindingMeta scan(){
        final Class<?> subviewClass = getViewModel().getClass();

        this.observableCollection = ViewBindingMetaUtil.scanClassObservableCollection(subviewClass);
        this.dataModelContext = ViewBindingMetaUtil.scanClassDataModelContext(subviewClass);
        this.eventSignals = ViewBindingMetaUtil.scanClassEventSignals(subviewClass);
        this.propertySlots = ViewBindingMetaUtil.scanClassPropertySlots(subviewClass);

        this.rootDataModelProperty = ConstantDataModelProperty.create(getDataModel());
        this.dataContextPath = dataModelContext.isPresent() ? dataModelContext.get().value() : "";

        return this;
    }

    @Override
    public Optional<ObservableCollection> getObservableCollection() {
        return this.observableCollection;
    }

    @Override
    public Optional<DataModelContext> getDataModelContext() {
        return this.dataModelContext;
    }

    @Override
    public Optional<EventSignals> getEventSignals() {
        return this.eventSignals;
    }

    @Override
    public Optional<PropertySlots> getPropertySlots() {
        return this.propertySlots;
    }

    @Override
	public boolean resolveDataModelChain(final LinkedList<DataModelProperty> dataModelChain) {
		dataModelChain.add(this.rootDataModelProperty);
		return appendDataModelPropertyChain(dataModelChain,this.dataContextPath);
	}
}

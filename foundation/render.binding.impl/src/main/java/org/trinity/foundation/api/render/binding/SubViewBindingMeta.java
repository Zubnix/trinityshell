package org.trinity.foundation.api.render.binding;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.lang.reflect.Field;
import java.util.LinkedList;

@Immutable
@AutoValue
public abstract class SubViewBindingMeta extends ViewBindingMeta {

    private Optional<ObservableCollection> observableCollection;
    private Optional<DataModelContext> dataModelContext;
    private Optional<EventSignals> eventSignals;
    private Optional<PropertySlots> propertySlots;
    private String          dataContextPath;

    public static ViewBindingMeta create(@Nonnull final ViewBindingMeta parentViewBindingMeta,
                                         @Nonnull final Field subviewField,
                                         @Nonnull final Object subviewValue) {

        return new AutoValue_SubViewBindingMeta(subviewValue,
                parentViewBindingMeta,
                subviewField).scan();
    }

    ViewBindingMeta scan(){

        final Class<?> subviewClass = getViewModel().getClass();

        this.observableCollection = ViewBindingMetaUtil.scanFieldObservableCollection(getSubViewField()).or(ViewBindingMetaUtil.scanClassObservableCollection(subviewClass));
        this.dataModelContext = ViewBindingMetaUtil.scanFieldDataModelContext(getSubViewField()).or(ViewBindingMetaUtil.scanClassDataModelContext(subviewClass));
        this.eventSignals = ViewBindingMetaUtil.scanFieldEventSignals(getSubViewField()).or(ViewBindingMetaUtil.scanClassEventSignals(subviewClass));
        this.propertySlots = ViewBindingMetaUtil.scanFieldPropertySlots(getSubViewField()).or(ViewBindingMetaUtil.scanClassPropertySlots(subviewClass));

        this.dataContextPath = this.dataModelContext.isPresent() ? this.dataModelContext.get().value() : "";

        return this;
    }

    public abstract Object getViewModel();

    public abstract ViewBindingMeta getParentViewBindingMeta();

    public abstract Field getSubViewField();

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
		final Boolean parentSuccess = getParentViewBindingMeta().resolveDataModelChain(dataModelChain);
		Boolean success = Boolean.FALSE;
		if(parentSuccess) {
			success = appendDataModelPropertyChain(dataModelChain, this.dataContextPath);
		}
		return parentSuccess && success;
	}
}
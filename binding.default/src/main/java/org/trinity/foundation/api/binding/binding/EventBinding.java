package org.trinity.foundation.api.binding.binding;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Optional;
import dagger.ObjectGraph;
import org.trinity.binding.api.view.EventSignal;
import org.trinity.binding.api.view.EventSignalFilter;
import org.trinity.binding.api.view.delegate.Signal;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoFactory
public class EventBinding implements ViewBinding {
    private final ObjectGraph injector;
    private final SignalFactory signalFactory;
    private final ViewBindingMeta viewBindingMeta;
    private final EventSignal eventSignal;

    private Optional<Signal> optionalSignal = Optional.absent();

    EventBinding(@Provided final ObjectGraph injector,
                 @Provided final SignalFactory signalFactory,
                 @Nonnull final ViewBindingMeta viewBindingMeta,
                 @Nonnull final EventSignal eventSignal) {
        checkNotNull(viewBindingMeta);
        checkNotNull(eventSignal);

        this.injector = injector;
        this.signalFactory = signalFactory;
        this.viewBindingMeta = viewBindingMeta;
        this.eventSignal = eventSignal;
    }

    @Nonnull
    @Override
    public ViewBindingMeta getViewBindingMeta() {
        return this.viewBindingMeta;
    }

    @Nonnull
    @Override
    public Collection<DataModelProperty> bind() {

        final LinkedList<DataModelProperty> properties = new LinkedList<>();
        final boolean commonDataModelContextResolved = this.viewBindingMeta.resolveDataModelChain(properties);

        if (!commonDataModelContextResolved) {
            return properties;
        }

        final String collectionDataModelContext = this.eventSignal.dataModelContext();
        final boolean dataModelContextResolved = this.viewBindingMeta.appendDataModelPropertyChain(properties,
                collectionDataModelContext);

        if (!dataModelContextResolved) {
            return properties;
        }

        final Optional<Object> lastPropertyValue = properties.getLast().getPropertyValue();

        if (lastPropertyValue.isPresent()) {
            bindEventSignal(lastPropertyValue.get());
        }

        return properties;
    }

    private void bindEventSignal(final Object eventReceiver) {
        final EventSignalFilter eventSignalFilter = this.injector.get(this.eventSignal.filter());
        final Signal signal = this.signalFactory.create(eventReceiver, this.eventSignal.name());
        this.optionalSignal = Optional.of(signal);
        eventSignalFilter.installFilter(this.viewBindingMeta.getViewModel(),
                signal);
    }

    @Override
    public void unbind() {
        if (this.optionalSignal.isPresent()) {
            final EventSignalFilter eventSignalFilter = this.injector.get(this.eventSignal.filter());
            eventSignalFilter.uninstallFilter(this.viewBindingMeta.getViewModel(),
                    this.optionalSignal.get());
        }
    }
}

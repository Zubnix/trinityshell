package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.Callable;

public class ViewBinderImpl2 implements ViewBinder {

    private final ViewBindingsTraverser viewBindingsTraverser;

    private final Multimap<DataModelProperty, AbstractViewBinding> dataModelPopertyToViewBindings = HashMultimap.create();
    private final Multimap<Object, AbstractViewBinding> viewModelToViewBindings = HashMultimap.create();

    @Inject
    ViewBinderImpl2(final ViewBindingsTraverser viewBindingsTraverser) {
        this.viewBindingsTraverser = viewBindingsTraverser;
    }

    @Override
    public ListenableFuture<Void> updateDataModelBinding(@Nonnull final ListeningExecutorService dataModelExecutor,
                                                         @Nonnull final Object dataModel,
                                                         @Nonnull final String propertyName,
                                                         @Nonnull final Optional<Object> oldPropertyValue,
                                                         @Nonnull final Optional<Object> newPropertyValue) {
        return dataModelExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                updateDataModelBinding(dataModel,
                                       propertyName,
                                       oldPropertyValue,
                                       newPropertyValue);
                return null;
            }
        });
    }

    public void updateDataModelBinding(@Nonnull final Object dataModel,
                                       @Nonnull final String propertyName,
                                       @Nonnull final Optional<Object> oldPropertyValue,
                                       @Nonnull final Optional<Object> newPropertyValue) {
        if(oldPropertyValue.equals(newPropertyValue)) {
            return;
        }

        final DataModelProperty dataModelProperty = new DataModelPropertyImpl(dataModel,
                                                                              propertyName);
        final Collection<AbstractViewBinding> abstractViewBindings = this.dataModelPopertyToViewBindings
                .removeAll(dataModelProperty);
        for(final AbstractViewBinding abstractViewBinding : abstractViewBindings) {
            abstractViewBinding.unbind();
            final Collection<DataModelProperty> newDataModelProperties = abstractViewBinding.bind();
            for(final DataModelProperty newDataModelProperty : newDataModelProperties) {
                this.dataModelPopertyToViewBindings.put(newDataModelProperty,
                                                        abstractViewBinding);
            }
        }
    }

    @Override
    public ListenableFuture<Void> bind(@Nonnull final ListeningExecutorService dataModelExecutor,
                                       @Nonnull final Object dataModel,
                                       @Nonnull final Object viewModel) {
        return dataModelExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                bind(dataModel,
                     viewModel);
                return null;
            }
        });
    }

    private void bind(@Nonnull final Object dataModel,
                      @Nonnull final Object viewModel) {
        final ViewBindingMeta viewBindingMeta = ViewBindingMeta.create(dataModel,
                                                                       viewModel);
        updateBindings(viewBindingMeta);
    }

    private void updateBindings(final ViewBindingMeta rootViewBindingMeta) {
        final FluentIterable<ViewBindingMeta> viewBindingMetas = this.viewBindingsTraverser
                .preOrderTraversal(rootViewBindingMeta);

        for(final ViewBindingMeta viewBindingMeta : viewBindingMetas) {
            createBindings(viewBindingMeta);
        }
    }

    private void createBindings(final ViewBindingMeta bindingMeta) {

        //create property binding
        if(bindingMeta.getPropertySlots().isPresent()) {
            final PropertyBinding propertyBinding = new PropertyBinding(bindingMeta);
            final Collection<DataModelProperty> dataModelProperties = propertyBinding.bind();
            for(final DataModelProperty dataModelProperty : dataModelProperties) {
                this.dataModelPopertyToViewBindings.put(dataModelProperty,
                                                        propertyBinding);
            }
            this.viewModelToViewBindings.put(bindingMeta.getViewModel(),
                                             propertyBinding);
        }

        //bind collection
        if(bindingMeta.getObservableCollection().isPresent()) {
            final CollectionBinding collectionBinding = new CollectionBinding(bindingMeta);
            final Collection<DataModelProperty> dataModelProperties = collectionBinding.bind();
            for(final DataModelProperty dataModelProperty : dataModelProperties) {
                this.dataModelPopertyToViewBindings.put(dataModelProperty,
                                                        collectionBinding);
            }
            this.viewModelToViewBindings.put(bindingMeta.getViewModel(),
                                             collectionBinding);
        }

        //bind view event
        if(bindingMeta.getEventSignals().isPresent()) {
            final EventBinding eventBinding = new EventBinding(bindingMeta);
            final Collection<DataModelProperty> dataModelProperties = eventBinding.bind();
            for(final DataModelProperty dataModelProperty : dataModelProperties) {
                this.dataModelPopertyToViewBindings.put(dataModelProperty,
                                                        eventBinding);
            }
            this.viewModelToViewBindings.put(bindingMeta.getViewModel(),
                                             eventBinding);
        }
    }

    @Override
    public ListenableFuture<Void> updateViewModelBinding(@Nonnull final ListeningExecutorService dataModelExecutor,
                                                         @Nonnull final Object parentViewModel,
                                                         @Nonnull final String fieldName,
                                                         @Nonnull final Optional<Object> oldSubView,
                                                         @Nonnull final Optional<Object> newSubView) {
        return dataModelExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                updateViewModelBinding(parentViewModel,
                                       fieldName,
                                       oldSubView,
                                       newSubView);
                return null;
            }
        });
    }

    private void updateViewModelBinding(@Nonnull final Object parentViewModel,
                                        @Nonnull final String fieldName,
                                        @Nonnull final Optional<Object> oldSubView,
                                        @Nonnull final Optional<Object> newSubView) {

    }
}

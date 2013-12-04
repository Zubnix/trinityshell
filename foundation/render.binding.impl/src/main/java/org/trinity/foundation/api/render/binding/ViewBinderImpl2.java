package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Table;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.Callable;

public class ViewBinderImpl2 implements ViewBinder {

    private static final Logger LOG = LoggerFactory.getLogger(ViewBinderImpl2.class);

    private static final Table<Class<?>, String, Optional<Field>> FIELD_CACHE = HashBasedTable.create();

    private final ViewBindingsTraverser viewBindingsTraverser;

    private final Multimap<DataModelProperty, AbstractViewBinding> dataModelPopertyToViewBindings = HashMultimap.create();
    private final Multimap<Object, ViewBindingMeta> viewModelToViewBindingMetas = HashMultimap.create();
    private final Multimap<ViewBindingMeta, AbstractViewBinding> viewBindingMetaToAbstractViewBindings = HashMultimap.create();

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
        if (oldPropertyValue.equals(newPropertyValue)) {
            return;
        }

        final DataModelProperty dataModelProperty = new DataModelPropertyImpl(dataModel,
                propertyName);

        //find all impacted view bindings
        final Collection<AbstractViewBinding> abstractViewBindings = this.dataModelPopertyToViewBindings
                .removeAll(dataModelProperty);

        //find all data model properties that point to the impacted view binding
        final Multimap<AbstractViewBinding, DataModelProperty> viewBindingToDataModelProperties = HashMultimap.create();
        Multimaps.invertFrom(this.dataModelPopertyToViewBindings,
                viewBindingToDataModelProperties);

        //for each view binding that was impacted by the changed data model property.
        for (final AbstractViewBinding abstractViewBinding : abstractViewBindings) {
            //unbind the view binding
            abstractViewBinding.unbind();
            //remove all data model properties that were pointing to the view binding
            final Collection<DataModelProperty> dataModelProperties = viewBindingToDataModelProperties.get(abstractViewBinding);
            for (final DataModelProperty oldModelProperty : dataModelProperties) {
                this.dataModelPopertyToViewBindings.remove(oldModelProperty,
                        abstractViewBinding);
            }

            //rebind the view & update the data model properties that point to this view model
            bindViewBinding(abstractViewBinding);
        }
    }

    private void bindViewBinding(final AbstractViewBinding abstractViewBinding) {
        //bind the view binding
        final Collection<DataModelProperty> newDataModelProperties = abstractViewBinding.bind();
        //link these new data model properties to this view binding
        for (final DataModelProperty newDataModelProperty : newDataModelProperties) {
            this.dataModelPopertyToViewBindings.put(newDataModelProperty,
                    abstractViewBinding);
        }
        //link the binding description to the created binding
        this.viewBindingMetaToAbstractViewBindings.put(abstractViewBinding.getViewBindingMeta(),
                abstractViewBinding);
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
        createAllBindings(viewBindingMeta);
    }

    private void createAllBindings(final ViewBindingMeta rootViewBindingMeta) {
        final FluentIterable<ViewBindingMeta> viewBindingMetas = this.viewBindingsTraverser.preOrderTraversal(rootViewBindingMeta);

        for (final ViewBindingMeta viewBindingMeta : viewBindingMetas) {
            createBindings(viewBindingMeta);
        }
    }

    private void createBindings(final ViewBindingMeta bindingMeta) {
        //if there are property slots
        if (bindingMeta.getPropertySlots().isPresent()) {
            final PropertyBinding propertyBinding = new PropertyBinding(bindingMeta);
            //create property binding
            bindViewBinding(propertyBinding);
        }

        //if there is an observable collection
        if (bindingMeta.getObservableCollection().isPresent()) {
            final CollectionBinding collectionBinding = new CollectionBinding(bindingMeta);
            //bind collection binding
            bindViewBinding(collectionBinding);
        }

        //if this view emits events
        if (bindingMeta.getEventSignals().isPresent()) {
            final EventBinding eventBinding = new EventBinding(bindingMeta);
            //bind view event
            bindViewBinding(eventBinding);
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
                                        @Nonnull final String subViewFieldName,
                                        @Nonnull final Optional<Object> oldSubView,
                                        @Nonnull final Optional<Object> newSubView) {
        if (oldSubView.equals(newSubView)) {
            return;
        }

        final Class<?> parentViewModelClass = parentViewModel.getClass();
        final Optional<Field> field = getField(parentViewModelClass,
                subViewFieldName);
        if (!field.isPresent()) {
            LOG.warn("Subview field name {} not found on object {}. Not updating subview.",
                    subViewFieldName,
                    parentViewModel);
            return;
        }

        //find all binding descriptions from the parent view
        final Collection<ViewBindingMeta> parentViewBindingMetas = this.viewModelToViewBindingMetas.get(parentViewModel);

        //if an old subview was present
        if (oldSubView.isPresent()) {
            //then for each of the parent binding descriptions
            for (final ViewBindingMeta parentViewBindingMeta : parentViewBindingMetas) {
                //recreate the old subview binding description
                final Optional<ViewBindingMeta> oldBindingMeta = ViewBindingMeta.create(parentViewBindingMeta,
                        field.get(),
                        oldSubView.get());
                if (oldBindingMeta.isPresent()) {
                    //use it to find all old view bindings
                    final Collection<AbstractViewBinding> oldAbstractViewBindings = this.viewBindingMetaToAbstractViewBindings.get(oldBindingMeta.get());
                    //and unbind them all
                    for (final AbstractViewBinding oldAbstractViewBinding : oldAbstractViewBindings) {
                        oldAbstractViewBinding.unbind();
                    }
                } else {
                    LOG.warn("Subview field with name {} is not marked as a SubView. Not updating subview.");
                }
            }
        }

        //if the new subview has a value
        if (newSubView.isPresent()) {
            //then for each of the parent binding descriptions
            for (final ViewBindingMeta parentViewBindingMeta : parentViewBindingMetas) {
                //create a new subview binding description
                final Optional<ViewBindingMeta> newBindingMeta = ViewBindingMeta.create(parentViewBindingMeta,
                        field.get(),
                        newSubView.get());
                if (newBindingMeta.isPresent()) {
                    //and create all bindings from this description.
                    createAllBindings(newBindingMeta.get());
                }
            }
        }
    }

    private Optional<Field> getField(final Class<?> viewModelClass,
                                     final String subViewFieldName) {
        Optional<Field> fieldOptional = FIELD_CACHE.get(viewModelClass,
                subViewFieldName);
        if (fieldOptional == null) {

            Field foundField = null;
            try {
                foundField = viewModelClass.getDeclaredField(subViewFieldName);
                foundField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                // TODO explanation
                LOG.error("",
                        e);
            }
            fieldOptional = Optional.fromNullable(foundField);
            FIELD_CACHE.put(viewModelClass,
                    subViewFieldName,
                    fieldOptional);
        }

        return fieldOptional;
    }
}

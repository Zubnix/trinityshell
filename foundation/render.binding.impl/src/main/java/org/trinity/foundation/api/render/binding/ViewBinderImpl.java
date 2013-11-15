/*******************************************************************************
 * Trinity Shell Copyright (C) 2011 Erik De Rijcke
 *
 * This file is part of Trinity Shell.
 *
 * Trinity Shell is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Trinity Shell is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 ******************************************************************************/

package org.trinity.foundation.api.render.binding;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Table;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.apache.onami.autobind.annotations.Bind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertyAdapter;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;
import org.trinity.foundation.api.render.binding.view.SubView;
import org.trinity.foundation.api.render.binding.view.delegate.PropertySlotInvocationDelegate;
import org.trinity.foundation.api.render.binding.view.delegate.SubViewModelDelegate;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.util.concurrent.Futures.addCallback;
import static java.lang.String.format;

//TODO refactor and split this huge class up.
@Bind
@Singleton
@ThreadSafe
public class ViewBinderImpl implements ViewBinder {

	private static final Logger LOG = LoggerFactory.getLogger(ViewBinderImpl.class);
    private static final Table<Class<?>, String, Optional<Method>> GETTER_CACHE = HashBasedTable.create();
    private static final Map<Class<?>, Field[]> DECLARED_FIELDS_CACHE = new HashMap<>();
    private static final String GET_BOOLEAN_PREFIX = "is";
    private static final String GET_PREFIX = "get";

    private final PropertySlotInvocationDelegate propertySlotInvocationDelegate;
    private final Injector injector;
    private final SubViewModelDelegate subViewModelDelegate;

    //<ViewModel,DataModel>
    private final Map<Object, Object> dataModelByViewModel = new WeakHashMap<>();
    //<DataModel,Set<ViewModel>>
    private final SetMultimap<Object, Object> viewModelsByDataModel = HashMultimap.create();
    //<ViewModel,PropertySlots>
    private final Map<Object, PropertySlots> propertySlotsByViewModel = new WeakHashMap<>();
    //<ViewModel,ObservableCollection>
    private final Map<Object, ObservableCollection> observableCollectionByViewModel = new WeakHashMap<>();
    //<ViewModel,EventSignals>
    private final Map<Object, EventSignals> eventSignalsByViewModel = new WeakHashMap<>();
    //<DataModel,ViewModel,DataModelContext>
    private final Table<Object, Object, DataModelContext> dataModelContextByViewModelAndParentDataModel = HashBasedTable
            .create();
    //<ViewModel,SubViewName,SubViewModel>
    private final Table<Object, String, Object> subviewModelByNameAndViewModel = HashBasedTable.create();

    @Inject
    ViewBinderImpl(final Injector injector,
                   final PropertySlotInvocationDelegate propertySlotInvocationDelegate,
                   final SubViewModelDelegate subViewModelDelegate) {
        this.injector = injector;
        this.subViewModelDelegate = subViewModelDelegate;
        this.propertySlotInvocationDelegate = propertySlotInvocationDelegate;
    }

    @Override
    public ListenableFuture<Void> bind(@Nonnull final ListeningExecutorService dataModelExecutor,
                                       @Nonnull final Object dataModel,
                                       @Nonnull final Object viewModel) {
        checkNotNull(dataModelExecutor);
        checkNotNull(dataModel);
        checkNotNull(viewModel);

        return dataModelExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                bindImpl(dataModelExecutor,
                         dataModel,
                         viewModel);
                return null;
            }
        });

    }

    @Override
    public ListenableFuture<Void> updateViewModelBinding(@Nonnull final ListeningExecutorService dataModelExecutor,
                                                         @Nonnull final Object changedViewModel,
                                                         @Nonnull final String subViewName) {
        checkNotNull(dataModelExecutor);
        checkNotNull(changedViewModel);
        checkNotNull(subViewName);

        return dataModelExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                updateViewModelBindingImpl(dataModelExecutor,
                                           changedViewModel,
                                           subViewName);
                return null;
            }
        });
    }

    protected void updateViewModelBindingImpl(@Nonnull final ListeningExecutorService dataModelExecutor,
                                              @Nonnull final Object changedViewModel,
                                              @Nonnull final String subViewName) {
        try {
            final Field subViewField = changedViewModel.getClass().getField(subViewName);
            final Object dataModel = dataModelByViewModel.get(changedViewModel);
            if(dataModel == null) {
                //our 'parent' view model is not yet bind. we will be bound when our parent view model is bound.
                return;
            }

            bindSubViewElement(dataModelExecutor,
                               subViewField,
                               dataModel,
                               changedViewModel);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void bindImpl(final ListeningExecutorService modelExecutor,
                            final Object dataModel,
                            final Object viewModel) {
        LOG.debug("Bind dataModel={} to viewModel={}",
                  dataModel,
                  viewModel);

        bindViewElement(modelExecutor,
                        dataModel,
                        viewModel,
                        Optional.<DataModelContext>absent(),
                        Optional.<EventSignals>absent(),
                        Optional.<ObservableCollection>absent(),
                        Optional.<PropertySlots>absent());
    }

    @Override
    public ListenableFuture<Void> updateDataModelBinding(@Nonnull final ListeningExecutorService dataModelExecutor,
                                                         @Nonnull final Object changedDataModel,
                                                         @Nonnull final String propertyName) {
        checkNotNull(dataModelExecutor);
        checkNotNull(changedDataModel);
        checkNotNull(propertyName);

        return dataModelExecutor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                updateBindingImpl(dataModelExecutor,
                                  changedDataModel,
                                  propertyName);
                return null;
            }
        });
    }

    protected void updateBindingImpl(final ListeningExecutorService dataModelExecutor,
                                     final Object dataModel,
                                     final String propertyName) {
        LOG.debug("Update binding for dataModel={} of property={}",
                  dataModel,
                  propertyName);

        updateDataContextBinding(dataModelExecutor,
                                 dataModel,
                                 propertyName);
        updateProperties(dataModel,
                         propertyName);
    }

    protected void updateDataContextBinding(final ListeningExecutorService dataModelExecutor,
                                            final Object dataModel,
                                            final String propertyName) {
        //check if there's an associated view.
        if(!this.dataModelContextByViewModelAndParentDataModel.containsRow(dataModel)) {
            return;
        }

        for(final Entry<Object, DataModelContext> dataModelContextForViewModel : this
                .dataModelContextByViewModelAndParentDataModel
                .column(dataModel).entrySet()) {

            final Object viewModel = dataModelContextForViewModel.getKey();
            final DataModelContext dataModelContext = dataModelContextForViewModel.getValue();
            final Optional<DataModelContext> optionalDataModelContext = Optional.of(dataModelContext);
            final Optional<EventSignals> optionalInputSignals = Optional
                    .fromNullable(this.eventSignalsByViewModel.get(viewModel));
            final Optional<ObservableCollection> optionalObservableCollection = Optional
                    .fromNullable(this.observableCollectionByViewModel.get(viewModel));
            final Optional<PropertySlots> optionalPropertySlots = Optional.fromNullable(this.propertySlotsByViewModel
                                                                                                .get(viewModel));

            if(dataModelContext.value().startsWith(propertyName)) {

                bindViewElement(dataModelExecutor,
                                dataModel,
                                viewModel,
                                optionalDataModelContext,
                                optionalInputSignals,
                                optionalObservableCollection,
                                optionalPropertySlots);
            }
        }
    }

    protected void updateProperties(final Object dataModel,
                                    final String propertyName) {

        try {
            final Optional<Method> optionalGetter = findGetter(dataModel.getClass(),
                                                               propertyName);
            if(!optionalGetter.isPresent()) {
                return;
            }
            final Object propertyValue = optionalGetter.get().invoke(dataModel);
            final Set<Object> viewModels = this.viewModelsByDataModel.get(dataModel);
            for(final Object viewModel : viewModels) {
                final PropertySlots propertySlots = this.propertySlotsByViewModel.get(viewModel);
                if(propertySlots == null) {
                    continue;
                }
                for(final PropertySlot propertySlot : propertySlots.value()) {
                    final String propertySlotPropertyName = propertySlot.propertyName();
                    if(propertySlotPropertyName.equals(propertyName)) {
                        invokePropertySlot(viewModel,
                                           propertySlot,
                                           propertyValue);
                    }
                }
            }

        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO explanation
            LOG.error("",
                      e);
        }
    }

    protected void bindViewElement(final ListeningExecutorService dataModelExecutor,
                                   final Object inheritedDataModel,
                                   final Object viewModel,
                                   final Optional<DataModelContext> optionalFieldLevelDataModelContext,
                                   final Optional<EventSignals> optionalFieldLevelEventSignals,
                                   final Optional<ObservableCollection> optionalFieldLevelObservableCollection,
                                   final Optional<PropertySlots> optionalFieldLevelPropertySlots) {
        checkNotNull(inheritedDataModel);
        checkNotNull(viewModel);

        final Class<?> viewModelClass = viewModel.getClass();

        // check for class level annotations if field level annotations are
        // absent
        final Optional<DataModelContext> optionalDataModelContext = optionalFieldLevelDataModelContext.or(Optional
                                                                                                                  .<DataModelContext>fromNullable(viewModelClass
                                                                                                                                                          .getAnnotation(DataModelContext.class)));
        final Optional<EventSignals> optionalEventSignals = optionalFieldLevelEventSignals.or(Optional
                                                                                                      .<EventSignals>fromNullable(viewModelClass
                                                                                                                                          .getAnnotation(EventSignals.class)));
        final Optional<ObservableCollection> optionalObservableCollection = optionalFieldLevelObservableCollection
                .or(Optional.<ObservableCollection>fromNullable(viewModelClass
                                                                        .getAnnotation(ObservableCollection.class)));
        final Optional<PropertySlots> optionalPropertySlots = optionalFieldLevelPropertySlots.or(Optional
                                                                                                         .<PropertySlots>fromNullable(viewModelClass
                                                                                                                                              .getAnnotation(PropertySlots.class)));

        final Object dataModel;
        if(optionalDataModelContext.isPresent()) {
            final Optional<Object> optionalDataModel = getDataModelForView(inheritedDataModel,
                                                                           viewModel,
                                                                           optionalDataModelContext.get());
            if(optionalDataModel.isPresent()) {
                dataModel = optionalDataModel.get();
            } else {
                // no data context value available so we're not going to bind
                // the viewModel.
                return;
            }
        } else {
            dataModel = inheritedDataModel;
        }

        registerBinding(dataModel,
                        viewModel);

        if(optionalEventSignals.isPresent()) {
            final EventSignal[] eventSignals = optionalEventSignals.get().value();
            bindEventSignals(dataModelExecutor,
                             dataModel,
                             viewModel,
                             eventSignals);
        }

        if(optionalObservableCollection.isPresent()) {
            final ObservableCollection observableCollection = optionalObservableCollection.get();
            bindObservableCollection(dataModelExecutor,
                                     dataModel,
                                     viewModel,
                                     observableCollection);
        }

        if(optionalPropertySlots.isPresent()) {
            final PropertySlots propertySlots = optionalPropertySlots.get();
            bindPropertySlots(dataModel,
                              viewModel,
                              propertySlots);
        }

        bindSubViewElements(dataModelExecutor,
                            dataModel,
                            viewModel);
    }

    protected void bindObservableCollection(final ListeningExecutorService dataModelExecutor,
                                            final Object dataModel,
                                            final Object viewModel,
                                            final ObservableCollection observableCollection) {
        checkNotNull(dataModel);
        checkNotNull(viewModel);
        checkNotNull(observableCollection);

        try {
            final String collectionProperty = observableCollection.value();

            final Optional<Method> collectionGetter = findGetter(dataModel.getClass(),
                                                                 collectionProperty);
            if(!collectionGetter.isPresent()) {
                return;
            }

            final Object collection = collectionGetter.get().invoke(dataModel);

            checkArgument(collection instanceof EventList,
                          format("Observable collection must be bound to a property of type %s @ dataModel: %s, viewModel: %s, observable collection: %s",
                                 EventList.class.getName(),
                                 dataModel,
                                 viewModel,
                                 observableCollection));

            final EventList<?> collectionModel = (EventList<?>) collection;
            final Class<?> childViewModelClass = observableCollection.view();

            try {
                collectionModel.getReadWriteLock().readLock().lock();

                for(int i = 0; i < collectionModel.size(); i++) {
                    final Object childDataModel = collectionModel.get(i);
                    final ListenableFuture<?> futureChildView = this.subViewModelDelegate.newView(viewModel,
                                                                                                  childViewModelClass,
                                                                                                  i);
                    addCallback(futureChildView,
                                new FutureCallback<Object>() {
                                    @Override
                                    public void onSuccess(final Object childViewModel) {
                                        bindImpl(dataModelExecutor,
                                                 childDataModel,
                                                 childViewModel);

                                    }

                                    @Override
                                    public void onFailure(final Throwable t) {
                                        LOG.error("Error while creating new child viewModel.",
                                                  t);
                                    }
                                },
                                dataModelExecutor);
                }

                collectionModel.addListEventListener(new ListEventListener<Object>() {

                    // We use a shadow list because glazedlists does not
                    // give us the deleted object...
                    private final List<Object> shadowChildDataModelList = new ArrayList<>(collectionModel);

                    @Override
                    public void listChanged(final ListEvent<Object> listChanges) {
                        handleListChanged(dataModelExecutor,
                                          viewModel,
                                          childViewModelClass,
                                          this.shadowChildDataModelList,
                                          listChanges);
                    }
                });

            } finally {
                collectionModel.getReadWriteLock().readLock().unlock();
            }

        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO explanation
            LOG.error("",
                      e);
        }
    }

    protected void handleListEventDelete(final Object viewModel,
                                         final int sourceIndex,
                                         final List<Object> shadowChildDataModelList) {
        final Object removedChildDataModel = shadowChildDataModelList.remove(sourceIndex);
        checkNotNull(removedChildDataModel);

        final Set<Object> removedChildViewModels = this.viewModelsByDataModel.get(removedChildDataModel);
        for(final Object removedChildView : removedChildViewModels) {
            ViewBinderImpl.this.subViewModelDelegate.destroyView(viewModel,
                                                             removedChildView,
                                                             sourceIndex);
        }
        this.viewModelsByDataModel.removeAll(removedChildDataModel);
    }

    protected void handleListEventInsert(final List<Object> changeList,
                                         final int sourceIndex,
                                         final ListeningExecutorService dataModelExecutor,
                                         final Object viewModel,
                                         final Class<?> childViewModelClass,
                                         final List<Object> shadowChildDataModelList) {
        final Object childDataModel = changeList.get(sourceIndex);
        checkNotNull(childDataModel);

        shadowChildDataModelList.add(sourceIndex,
                                     childDataModel);

        final ListenableFuture<?> futureChildView = this.subViewModelDelegate.newView(viewModel,
                                                                                      childViewModelClass,
                                                                                      sourceIndex);
        addCallback(futureChildView,
                    new FutureCallback<Object>() {
                        @Override
                        public void onSuccess(final Object childViewModel) {
                            bindImpl(dataModelExecutor,
                                     childDataModel,
                                     childViewModel);

                        }

                        @Override
                        public void onFailure(final Throwable t) {
                            LOG.error("Error while creating new child viewModel.",
                                      t);
                        }
                    },
                    dataModelExecutor);
    }

    protected void handleListEventUpdate(final ListeningExecutorService dataModelExecutor,
                                         final Object viewModel,
                                         final List<Object> shadowChildDataModelList,
                                         final int sourceIndex,
                                         final List<Object> changeList,
                                         final ListEvent<Object> listChanges) {
        if(listChanges.isReordering()) {
            final int[] reorderings = listChanges.getReorderMap();
            for(int i = 0; i < reorderings.length; i++) {
                final int newPosition = reorderings[i];
                final Object childDataModel = changeList.get(sourceIndex);

                shadowChildDataModelList.clear();
                shadowChildDataModelList.add(newPosition,
                                             childDataModel);

                final Set<Object> changedChildViewModels = ViewBinderImpl.this.viewModelsByDataModel
                        .get(childDataModel);
                for(final Object changedChildviewModel : changedChildViewModels) {
                    this.subViewModelDelegate.updateChildViewPosition(viewModel,
                                                                      changedChildviewModel,
                                                                      i,
                                                                      newPosition);
                }
            }
        } else {

            final Object newChildDataModel = changeList.get(sourceIndex);
            final Object oldChildDataModel = shadowChildDataModelList.set(sourceIndex,
                                                                          newChildDataModel);
            checkNotNull(oldChildDataModel);
            checkNotNull(newChildDataModel);

            final Set<Object> childViewModels = this.viewModelsByDataModel.get(oldChildDataModel);
            this.viewModelsByDataModel.removeAll(oldChildDataModel);

            for(final Object changedChildViewModel : childViewModels) {
                bindImpl(dataModelExecutor,
                         newChildDataModel,
                         changedChildViewModel);
            }
        }
    }

    protected void handleListChanged(final ListeningExecutorService dataModelExecutor,
                                     final Object viewModel,
                                     final Class<?> childViewModelClass,
                                     final List<Object> shadowChildDataModelList,
                                     final ListEvent<Object> listChanges) {
        while(listChanges.next()) {
            final int sourceIndex = listChanges.getIndex();
            final int changeType = listChanges.getType();
            final List<Object> changeList = listChanges.getSourceList();

            switch(changeType) {
                case ListEvent.DELETE: {
                    handleListEventDelete(viewModel,
                                          sourceIndex,
                                          shadowChildDataModelList);
                    break;
                }
                case ListEvent.INSERT: {
                    handleListEventInsert(changeList,
                                          sourceIndex,
                                          dataModelExecutor,
                                          viewModel,
                                          childViewModelClass,
                                          shadowChildDataModelList);
                    break;
                }
                case ListEvent.UPDATE: {
                    handleListEventUpdate(dataModelExecutor,
                                          viewModel,
                                          shadowChildDataModelList,
                                          sourceIndex,
                                          changeList,
                                          listChanges);
                    break;
                }
            }
        }
    }

    protected void bindEventSignals(final ListeningExecutorService dataModelExecutor,
                                    final Object dataModel,
                                    final Object viewModel,
                                    final EventSignal[] eventSignals) {
        checkNotNull(dataModel);
        checkNotNull(viewModel);
        checkNotNull(eventSignals);

        for(final EventSignal eventSignal : eventSignals) {
            final Class<? extends EventSignalFilter> eventSignalFilterType = eventSignal.filter();
            final String inputSlotName = eventSignal.name();

            // FIXME cache filter & uninstall any previous filter installments
            final EventSignalFilter eventSignalFilter = this.injector.getInstance(eventSignalFilterType);
            eventSignalFilter.installFilter(viewModel,
                                            new SignalImpl(dataModelExecutor,
                                                           viewModel,
                                                           this.dataModelByViewModel,
                                                           inputSlotName));

        }
    }

    protected void registerBinding(final Object dataModel,
                                   final Object viewModel) {
        checkNotNull(dataModel);
        checkNotNull(viewModel);

        final Object oldDataModel = this.dataModelByViewModel.put(viewModel,
                                                                  dataModel);
        if(oldDataModel != null) {
            this.viewModelsByDataModel.remove(oldDataModel,
                                              viewModel);
        }
        viewModelsByDataModel.put(dataModel,
                                  viewModel);
    }

    protected void unregisterBinding(final Object viewModel) {
        checkNotNull(viewModel);

        final Object dataModel = this.dataModelByViewModel.remove(viewModel);
        this.viewModelsByDataModel.remove(dataModel,
                                          viewModel);

        this.observableCollectionByViewModel.remove(viewModel);
        this.propertySlotsByViewModel.remove(viewModel);
        this.eventSignalsByViewModel.remove(viewModel);
        this.dataModelContextByViewModelAndParentDataModel.columnMap().remove(viewModel);
    }

    protected void bindPropertySlots(final Object dataModel,
                                     final Object viewModel,
                                     final PropertySlots propertySlots) {
        checkNotNull(dataModel);
        checkNotNull(viewModel);
        checkNotNull(propertySlots);

        this.propertySlotsByViewModel.put(viewModel,
                                          propertySlots);
        for(final PropertySlot propertySlot : propertySlots.value()) {
            bindPropertySlot(dataModel,
                             viewModel,
                             propertySlot);
        }
    }

    protected void bindPropertySlot(final Object dataModel,
                                    final Object viewModel,
                                    final PropertySlot propertySlot) {
        checkNotNull(dataModel);
        checkNotNull(viewModel);
        checkNotNull(propertySlot);

        try {
            final String propertySlotDataModelContext = propertySlot.dataModelContext();
            final Object propertyDataModel;
            if(propertySlotDataModelContext.isEmpty()) {
                propertyDataModel = dataModel;
            } else {
                final Optional<Object> optionalRelativeDataModel = getDataModel(dataModel,
                                                                                propertySlotDataModelContext);
                if(optionalRelativeDataModel.isPresent()) {
                    propertyDataModel = optionalRelativeDataModel.get();
                } else {
                    return;
                }
            }
            final String propertyName = propertySlot.propertyName();
            final Optional<Method> optionalGetter = findGetter(propertyDataModel.getClass(),
                                                               propertyName);
            if(optionalGetter.isPresent()) {
                final Method getter = optionalGetter.get();

                // workaround for bug (4071957) submitted in
                // 1997(!) and still not fixed by sun/oracle.
                if(propertyDataModel.getClass().isAnonymousClass()) {
                    getter.setAccessible(true);
                }

                final Object propertyInstance = optionalGetter.get().invoke(propertyDataModel);

                invokePropertySlot(viewModel,
                                   propertySlot,
                                   propertyInstance);

            }
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO explanation
            LOG.error("",
                      e);
        }
    }

    protected void invokePropertySlot(final Object viewModel,
                                      final PropertySlot propertySlot,
                                      final Object propertyValue) {
        checkNotNull(viewModel);
        checkNotNull(propertySlot);
        checkNotNull(propertyValue);

        try {
            final String viewModelMethodName = propertySlot.methodName();
            final Class<?>[] viewMethodArgumentTypes = propertySlot.argumentTypes();
            final Method targetViewMethod = viewModel.getClass().getMethod(viewModelMethodName,
                                                                           viewMethodArgumentTypes);
            final Class<? extends PropertyAdapter<?>> propertyAdapterType = propertySlot.adapter();
            @SuppressWarnings("rawtypes")
            final PropertyAdapter propertyAdapter = propertyAdapterType.newInstance();
            @SuppressWarnings("unchecked")
            final Object argument = propertyAdapter.adapt(propertyValue);

            this.propertySlotInvocationDelegate.invoke(viewModel,
                                                       targetViewMethod,
                                                       argument);
        } catch(final NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException e) {
            // TODO explanation
            LOG.error("",
                      e);
        }
    }

    protected Optional<Object> getDataModelForView(final Object parentDataModel,
                                                   final Object viewModel,
                                                   final DataModelContext dataModelContext) {
        checkNotNull(parentDataModel);
        checkNotNull(viewModel);
        checkNotNull(dataModelContext);

        this.dataModelContextByViewModelAndParentDataModel.put(parentDataModel,
                                                               viewModel,
                                                               dataModelContext);

        final String propertyChain = dataModelContext.value();
        return getDataModel(parentDataModel,
                            propertyChain);
    }

    protected void bindSubViewElements(final ListeningExecutorService dataModelExecutor,
                                       final Object inheritedDataModel,
                                       final Object viewModel) {
        checkNotNull(inheritedDataModel);
        checkNotNull(viewModel);

        try {

            final Class<?> viewModelClass = viewModel.getClass();

            final Field[] viewModelFields = getFields(viewModelClass);

            for(final Field subViewModelField : viewModelFields) {
                bindSubViewElement(dataModelExecutor,
                                   subViewModelField,
                                   inheritedDataModel,
                                   viewModel);
            }
        } catch(IllegalArgumentException | IllegalAccessException e) {
            // TODO explanation
            LOG.error("",
                      e);
        }
    }

    protected void bindSubViewElement(final ListeningExecutorService dataModelExecutor,
                                      final Field subViewModelField,
                                      final Object inheritedDataModel,
                                      final Object viewModel) throws IllegalAccessException {
        checkNotNull(dataModelExecutor);
        checkNotNull(subViewModelField);
        checkNotNull(inheritedDataModel);
        checkNotNull(viewModel);

        //find any previously associated value and remove it
        final Object oldSubViewModel = subviewModelByNameAndViewModel.get(viewModel,
                                                                    subViewModelField.getName());
        if(oldSubViewModel != null) {
            unregisterBinding(oldSubViewModel);
        }

        final Object subViewModel = subViewModelField.get(viewModel);

        // filter out null values
        if(subViewModel == null) {
            return;
        }

        if(subViewModelField.getAnnotation(SubView.class) == null && subViewModel.getClass()
                                                                                 .getAnnotation(SubView.class) == null) {
            return;
        }

        // recursion safety
        if(this.dataModelByViewModel.containsKey(subViewModel)) {
            return;
        }

        //register new sub view model
        this.subviewModelByNameAndViewModel.put(viewModel,
                                                subViewModelField.getName(),
                                                subViewModel);

        final Optional<DataModelContext> optionalFieldDataContext = Optional
                .fromNullable(subViewModelField.getAnnotation(DataModelContext.class));
        final Optional<EventSignals> optionalFieldInputSignals = Optional
                .fromNullable(subViewModelField.getAnnotation(EventSignals.class));
        final Optional<ObservableCollection> optionalFieldObservableCollection = Optional
                .fromNullable(subViewModelField.getAnnotation(ObservableCollection.class));
        final Optional<PropertySlots> optionalFieldPropertySlots = Optional
                .fromNullable(subViewModelField.getAnnotation(PropertySlots.class));

        bindViewElement(dataModelExecutor,
                        inheritedDataModel,
                        subViewModel,
                        optionalFieldDataContext,
                        optionalFieldInputSignals,
                        optionalFieldObservableCollection,
                        optionalFieldPropertySlots);
    }

    protected Iterable<String> toPropertyNames(final String subModelPath) {
        checkNotNull(subModelPath);

        return Splitter.on('.').trimResults().omitEmptyStrings().split(subModelPath);
    }

    protected Optional<Object> getDataModel(final Object parentDataModel,
                                            final String propertyChain) {
        checkNotNull(parentDataModel);
        checkNotNull(propertyChain);

        final Iterable<String> propertyNames = toPropertyNames(propertyChain);

        Object currentDataModel = parentDataModel;
        try {

            for(final String propertyName : propertyNames) {
                if(currentDataModel == null) {
                    break;
                }
                final Class<?> currentModelClass = currentDataModel.getClass();
                final Optional<Method> foundMethod = findGetter(currentModelClass,
                                                                propertyName);
                if(foundMethod.isPresent()) {
                    currentDataModel = foundMethod.get().invoke(currentDataModel);
                }
            }
        } catch(final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            LOG.error(String.format("Can not access getter on %s. Is it a no argument public method?",
                                    currentDataModel),
                      e);
        }
        return Optional.fromNullable(currentDataModel);
    }

    protected Optional<Method> findGetter(final Class<?> dataModelClass,
                                          final String propertyName) {
        checkNotNull(dataModelClass);
        checkNotNull(propertyName);
        return getGetterMethod(dataModelClass,
                               propertyName);
    }

    protected Field[] getFields(final Class<?> clazz) {
        Field[] fields = DECLARED_FIELDS_CACHE.get(clazz);
        if(fields == null) {
            fields = clazz.getFields();
            DECLARED_FIELDS_CACHE.put(clazz,
                                      fields);
        }
        return fields;
    }

    protected Optional<Method> getGetterMethod(final Class<?> dataModelClass,
                                               final String propertyName) {

        Optional<Method> methodOptional = GETTER_CACHE.get(dataModelClass,
                                                           propertyName);

        if(methodOptional == null) {
            //initialized methodOptional and store it in cache

            Method foundMethod = null;
            String getterMethodName = toGetterMethodName(propertyName);

            try {
                foundMethod = dataModelClass.getMethod(getterMethodName);
            } catch(final NoSuchMethodException e) {
                // no getter with get found,
                // try with is.
                getterMethodName = toBooleanGetterMethodName(propertyName);
                try {
                    foundMethod = dataModelClass.getMethod(getterMethodName);
                } catch(final NoSuchMethodException e1) {
                    // TODO explanation
                    LOG.error("",
                              e1);

                } catch(final SecurityException e1) {
                    LOG.error(format("Property %s is not accessible on %s. Did you declare it as public?",
                                     propertyName,
                                     dataModelClass.getName()),
                              e);
                }
            } catch(final SecurityException e1) {
                // TODO explanation
                LOG.error(format("Property %s is not accessible on %s. Did you declare it as public?",
                                 propertyName,
                                 dataModelClass.getName()),
                          e1);
            }
            methodOptional = Optional.fromNullable(foundMethod);

            GETTER_CACHE.put(dataModelClass,
                             propertyName,
                             methodOptional);
        }

        return methodOptional;
    }

    protected String toGetterMethodName(final String propertyName) {
        return toGetterMethodName(GET_PREFIX,
                                  propertyName);
    }

    protected String toGetterMethodName(final String prefix,
                                        final String propertyName) {
        checkNotNull(prefix);
        checkNotNull(propertyName);

        return prefix + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,
                                                  propertyName);
    }

    protected String toBooleanGetterMethodName(final String propertyName) {
        return toGetterMethodName(GET_BOOLEAN_PREFIX,
                                  propertyName);
    }
}
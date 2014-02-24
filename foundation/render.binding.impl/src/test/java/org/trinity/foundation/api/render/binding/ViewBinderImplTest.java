package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.TreeTraverser;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlot;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static java.lang.Boolean.TRUE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TreeTraverser.class, ViewBindingMeta.class})
public class ViewBinderImplTest {

    @Mock
    private ViewBindingsTraverser viewBindingsTraverser;
    @Mock
    private CollectionBindingFactory collectionBindingFactory;
    @Mock
    private EventBindingFactory eventBindingFactory;
    @Mock
    private PropertyBindingFactory propertyBindingFactory;
    @InjectMocks
    private ViewBinderImpl viewBinderImpl;

    @Test
    public void testBind() {
        //given
        //a view binder
        //a root data model
        //a view model
        //a nested view model
        //a deep nested view model
        final Object dataModel = mock(Object.class);
        final Object viewModel = mock(Object.class);
        final Object nestedViewModel = mock(Object.class);
        final Object deepNestedViewModel = mock(Object.class);
        final ListeningExecutorService dataModelExecutor = mock(ListeningExecutorService.class);
        when(dataModelExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object arg0 = invocation.getArguments()[0];
                final Callable<?> submittedCallable = (Callable<?>) arg0;
                final Object result = submittedCallable.call();
                return immediateFuture(result);
            }
        });

        final DataModelContext dataModelContext = mock(DataModelContext.class);
        final EventSignals eventSignals = mock(EventSignals.class);
        final EventSignal eventSignal = mock(EventSignal.class);
        final EventSignal[] eventSignalsValue = new EventSignal[]{eventSignal};
        when(eventSignals.value()).thenReturn(eventSignalsValue);
        final ObservableCollection observableCollection = mock(ObservableCollection.class);
        final PropertySlots propertySlots = mock(PropertySlots.class);
        final PropertySlot propertySlot = mock(PropertySlot.class);
        final PropertySlot[] propertySlotsValue = new PropertySlot[]{propertySlot};
        when(propertySlots.value()).thenReturn(propertySlotsValue);

        //root view binding meta
        final ViewBindingMeta rootViewBindingMeta = mock(ViewBindingMeta.class);
        when(rootViewBindingMeta.getViewModel()).thenReturn(viewModel);
        when(rootViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(dataModelContext));
        when(rootViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(rootViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(rootViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        //nested view binding meta
        final ViewBindingMeta nestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(nestedViewBindingMeta.getViewModel()).thenReturn(nestedViewModel);
        when(nestedViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(dataModelContext));
        when(nestedViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(nestedViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(nestedViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        //deep nested view binding meta
        final ViewBindingMeta deepNestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(deepNestedViewBindingMeta.getViewModel()).thenReturn(deepNestedViewModel);
        when(deepNestedViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(dataModelContext));
        when(deepNestedViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(deepNestedViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(deepNestedViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        //root view binding meta creation
        mockStatic(ViewBindingMeta.class);
        when(ViewBindingMeta.create(dataModel, viewModel)).thenReturn(rootViewBindingMeta);

        //created root bindings
        final CollectionBinding collectionBinding = mock(CollectionBinding.class);
        final EventBinding eventBinding = mock(EventBinding.class);
        final PropertyBinding propertyBinding = mock(PropertyBinding.class);

        //created bindings for root view binding meta
        when(this.collectionBindingFactory.create(rootViewBindingMeta, observableCollection)).thenReturn(collectionBinding);
        when(this.eventBindingFactory.create(rootViewBindingMeta, eventSignal)).thenReturn(eventBinding);
        when(this.propertyBindingFactory.create(rootViewBindingMeta, propertySlot)).thenReturn(propertyBinding);

        //created nested bindings
        final CollectionBinding nestedCollectionBinding = mock(CollectionBinding.class);
        final EventBinding nestedEventBinding = mock(EventBinding.class);
        final PropertyBinding nestedPropertyBinding = mock(PropertyBinding.class);

        //created bindings for nested view binding meta
        when(this.collectionBindingFactory.create(nestedViewBindingMeta, observableCollection)).thenReturn(nestedCollectionBinding);
        when(this.eventBindingFactory.create(nestedViewBindingMeta, eventSignal)).thenReturn(nestedEventBinding);
        when(this.propertyBindingFactory.create(nestedViewBindingMeta, propertySlot)).thenReturn(nestedPropertyBinding);

        //created deep nested bindings
        final CollectionBinding deepNestedCollectionBinding = mock(CollectionBinding.class);
        final EventBinding deepNestedEventBinding = mock(EventBinding.class);
        final PropertyBinding deepNestedPropertyBinding = mock(PropertyBinding.class);

        //created bindings for deep nested view binding meta
        when(this.collectionBindingFactory.create(deepNestedViewBindingMeta, observableCollection)).thenReturn(deepNestedCollectionBinding);
        when(this.eventBindingFactory.create(deepNestedViewBindingMeta, eventSignal)).thenReturn(deepNestedEventBinding);
        when(this.propertyBindingFactory.create(deepNestedViewBindingMeta, propertySlot)).thenReturn(deepNestedPropertyBinding);

        //view binding meta discovery
        final FluentIterable<ViewBindingMeta> bindingMetas = FluentIterable.from(Arrays.asList(rootViewBindingMeta, nestedViewBindingMeta, deepNestedViewBindingMeta));

        when(this.viewBindingsTraverser.preOrderTraversal((ViewBindingMeta) any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object[] args = invocation.getArguments();
                final ViewBindingMeta arg0 = (ViewBindingMeta) args[0];
                if (arg0 == rootViewBindingMeta) {
                    return bindingMetas;
                } else {
                    return FluentIterable.from(Arrays.asList());
                }
            }
        });

        //when
        //the view model is bound to the data model
        this.viewBinderImpl.bind(dataModel, viewModel);

        //then
        //all defined bindings in the view model are created and bound to the data model
        verify(this.collectionBindingFactory).create(rootViewBindingMeta, observableCollection);
        verify(this.eventBindingFactory).create(rootViewBindingMeta, eventSignal);
        verify(this.propertyBindingFactory).create(rootViewBindingMeta, propertySlot);

        verify(this.collectionBindingFactory).create(rootViewBindingMeta, observableCollection);
        verify(this.eventBindingFactory).create(rootViewBindingMeta, eventSignal);
        verify(this.propertyBindingFactory).create(nestedViewBindingMeta, propertySlot);

        verify(this.collectionBindingFactory).create(rootViewBindingMeta, observableCollection);
        verify(this.eventBindingFactory).create(rootViewBindingMeta, eventSignal);
        verify(this.propertyBindingFactory).create(deepNestedViewBindingMeta, propertySlot);

        verify(collectionBinding).bind();
        verify(eventBinding).bind();
        verify(propertyBinding).bind();

        verify(nestedCollectionBinding).bind();
        verify(nestedEventBinding).bind();
        verify(nestedPropertyBinding).bind();

        verify(deepNestedCollectionBinding).bind();
        verify(deepNestedEventBinding).bind();
        verify(deepNestedPropertyBinding).bind();
    }

    @Test
    public void testUpdateDataModelBindingWasNull() {
        //given
        //a view binder
        //a data model with properties
        //a view model that has a datacontext
        //a nested view model that has a datacontext
        //a deep nested view model that has a datacontext

        final Object dataModel = mock(Object.class);
        final Object viewModel = mock(Object.class);
        final Object nestedViewModel = mock(Object.class);
        final Object deepNestedViewModel = mock(Object.class);
        final ListeningExecutorService dataModelExecutor = mock(ListeningExecutorService.class);
        when(dataModelExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object arg0 = invocation.getArguments()[0];
                final Callable<?> submittedCallable = (Callable<?>) arg0;
                final Object result = submittedCallable.call();
                return immediateFuture(result);
            }
        });


        final EventSignals eventSignals = mock(EventSignals.class);
        final EventSignal eventSignal = mock(EventSignal.class);
        final EventSignal[] eventSignalsValue = new EventSignal[]{eventSignal};
        when(eventSignals.value()).thenReturn(eventSignalsValue);
        final ObservableCollection observableCollection = mock(ObservableCollection.class);
        final PropertySlots propertySlots = mock(PropertySlots.class);
        final PropertySlot propertySlot = mock(PropertySlot.class);
        final PropertySlot[] propertySlotsValue = new PropertySlot[]{propertySlot};
        when(propertySlots.value()).thenReturn(propertySlotsValue);

        //root view binding meta
        final DataModelContext rootDataModelContext = mock(DataModelContext.class);
        when(rootDataModelContext.value()).thenReturn("foo");
        final ViewBindingMeta rootViewBindingMeta = mock(ViewBindingMeta.class);
        when(rootViewBindingMeta.getViewModel()).thenReturn(viewModel);
        when(rootViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(rootDataModelContext));
        when(rootViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(rootViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(rootViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        //nested view binding meta
        final DataModelContext nestedDataModelContext = mock(DataModelContext.class);
        when(nestedDataModelContext.value()).thenReturn("foo.bar");
        final ViewBindingMeta nestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(nestedViewBindingMeta.getViewModel()).thenReturn(nestedViewModel);
        when(nestedViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(nestedDataModelContext));
        when(nestedViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(nestedViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(nestedViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        //deep nested view binding meta
        final DataModelContext deepNestedDataModelContext = mock(DataModelContext.class);
        when(deepNestedDataModelContext.value()).thenReturn("foo.bar.baz");
        final ViewBindingMeta deepNestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(deepNestedViewBindingMeta.getViewModel()).thenReturn(deepNestedViewModel);
        when(deepNestedViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(deepNestedDataModelContext));
        when(deepNestedViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(deepNestedViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(deepNestedViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        //root view binding meta creation
        mockStatic(ViewBindingMeta.class);
        when(ViewBindingMeta.create(dataModel, viewModel)).thenReturn(rootViewBindingMeta);

        //updated foo value
        final Object fooValue = new Object();

        //created root bindings
        final CollectionBinding rootCollectionBinding = mock(CollectionBinding.class);
        final EventBinding rootEventBinding = mock(EventBinding.class);
        final PropertyBinding rootPropertyBinding = mock(PropertyBinding.class);

        final List<DataModelProperty> rootPropertiesOnBind = Arrays.asList(new ConstantDataModelProperty(dataModel), new RelativeDataModelProperty(dataModel, "foo"));
        final List<DataModelProperty> rootPropertiesOnUpdate = Arrays.asList(new ConstantDataModelProperty(dataModel), new RelativeDataModelProperty(dataModel, "foo"), new RelativeDataModelProperty(fooValue, "bar"));
        when(rootCollectionBinding.bind()).thenReturn(rootPropertiesOnBind, rootPropertiesOnUpdate);
        when(rootEventBinding.bind()).thenReturn(rootPropertiesOnBind, rootPropertiesOnUpdate);
        when(rootPropertyBinding.bind()).thenReturn(rootPropertiesOnBind, rootPropertiesOnUpdate);

        //created bindings for root view binding meta
        when(this.collectionBindingFactory.create(rootViewBindingMeta, observableCollection)).thenReturn(rootCollectionBinding);
        when(this.eventBindingFactory.create(rootViewBindingMeta, eventSignal)).thenReturn(rootEventBinding);
        when(this.propertyBindingFactory.create(rootViewBindingMeta, propertySlot)).thenReturn(rootPropertyBinding);

        //created nested bindings
        final CollectionBinding nestedCollectionBinding = mock(CollectionBinding.class);
        final EventBinding nestedEventBinding = mock(EventBinding.class);
        final PropertyBinding nestedPropertyBinding = mock(PropertyBinding.class);

        final List<DataModelProperty> nestedPropertiesOnBind = Arrays.asList(new ConstantDataModelProperty(dataModel), new RelativeDataModelProperty(dataModel, "foo"));
        final List<DataModelProperty> nestedPropertiesOnUpdate = Arrays.asList(new ConstantDataModelProperty(dataModel), new RelativeDataModelProperty(dataModel, "foo"), new RelativeDataModelProperty(fooValue, "bar"));
        when(nestedCollectionBinding.bind()).thenReturn(nestedPropertiesOnBind, nestedPropertiesOnUpdate);
        when(nestedEventBinding.bind()).thenReturn(nestedPropertiesOnBind, nestedPropertiesOnUpdate);
        when(nestedPropertyBinding.bind()).thenReturn(nestedPropertiesOnBind, nestedPropertiesOnUpdate);

        //created bindings for nested view binding meta
        when(this.collectionBindingFactory.create(nestedViewBindingMeta, observableCollection)).thenReturn(nestedCollectionBinding);
        when(this.eventBindingFactory.create(nestedViewBindingMeta, eventSignal)).thenReturn(nestedEventBinding);
        when(this.propertyBindingFactory.create(nestedViewBindingMeta, propertySlot)).thenReturn(nestedPropertyBinding);

        //created deep nested bindings
        final CollectionBinding deepNestedCollectionBinding = mock(CollectionBinding.class);
        final EventBinding deepNestedEventBinding = mock(EventBinding.class);
        final PropertyBinding deepNestedPropertyBinding = mock(PropertyBinding.class);

        final List<DataModelProperty> deepNestedPropertiesOnBind = Arrays.asList(new ConstantDataModelProperty(dataModel), new RelativeDataModelProperty(dataModel, "foo"));
        final List<DataModelProperty> deepNestedPropertiesOnUpdate = Arrays.asList(new ConstantDataModelProperty(dataModel), new RelativeDataModelProperty(dataModel, "foo"), new RelativeDataModelProperty(fooValue, "bar"));
        when(deepNestedCollectionBinding.bind()).thenReturn(deepNestedPropertiesOnBind, deepNestedPropertiesOnUpdate);
        when(deepNestedEventBinding.bind()).thenReturn(deepNestedPropertiesOnBind, deepNestedPropertiesOnUpdate);
        when(deepNestedPropertyBinding.bind()).thenReturn(deepNestedPropertiesOnBind, deepNestedPropertiesOnUpdate);

        //created bindings for deep nested view binding meta
        when(this.collectionBindingFactory.create(deepNestedViewBindingMeta, observableCollection)).thenReturn(deepNestedCollectionBinding);
        when(this.eventBindingFactory.create(deepNestedViewBindingMeta, eventSignal)).thenReturn(deepNestedEventBinding);
        when(this.propertyBindingFactory.create(deepNestedViewBindingMeta, propertySlot)).thenReturn(deepNestedPropertyBinding);

        //view binding meta discovery
        //view binding meta discovery
        final FluentIterable<ViewBindingMeta> bindingMetas = FluentIterable.from(Arrays.asList(rootViewBindingMeta, nestedViewBindingMeta, deepNestedViewBindingMeta));

        when(this.viewBindingsTraverser.preOrderTraversal((ViewBindingMeta) any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object[] args = invocation.getArguments();
                final ViewBindingMeta arg0 = (ViewBindingMeta) args[0];
                if (arg0 == rootViewBindingMeta) {
                    return bindingMetas;
                } else {
                    return FluentIterable.from(Arrays.asList());
                }
            }
        });

        this.viewBinderImpl.bind(dataModel, viewModel);

        //when
        //a property of the data model is updated
        this.viewBinderImpl.updateDataModelBinding(dataModel, "foo", Optional.absent(), Optional.of(fooValue));

        //then
        //the impacted view bindings are refreshed (unbind+bind)

        //root initial bind + refresh bind
        verify(rootCollectionBinding,times(2)).bind();
        verify(rootEventBinding,times(2)).bind();
        verify(rootPropertyBinding,times(2)).bind();

        //root refresh unbind
        verify(rootCollectionBinding).unbind();
        verify(rootEventBinding).unbind();
        verify(rootPropertyBinding).unbind();

        //nested initial bind + refresh bind
        verify(nestedCollectionBinding,times(2)).bind();
        verify(nestedEventBinding,times(2)).bind();
        verify(nestedPropertyBinding,times(2)).bind();

        //nested refresh unbind
        verify(nestedCollectionBinding).unbind();
        verify(nestedEventBinding).unbind();
        verify(nestedPropertyBinding).unbind();

        //deep nested initial bind + refresh bind
        verify(deepNestedCollectionBinding,times(2)).bind();
        verify(deepNestedEventBinding,times(2)).bind();
        verify(deepNestedPropertyBinding,times(2)).bind();

        //deep nested refresh unbind
        verify(deepNestedCollectionBinding).unbind();
        verify(deepNestedEventBinding).unbind();
        verify(deepNestedPropertyBinding).unbind();
    }

    @Test
    public void testUpdateViewModelBinding() {
        //given
        //a view binder
        //a root data model
        //a view model
        //a nested view model
        //a deep nested view model
        final Object dataModel = mock(Object.class);
        final Object viewModel = mock(Object.class);
        final Object nestedViewModel = mock(Object.class);
        final Object deepNestedViewModel = mock(Object.class);
        final Object newNestedViewModel = mock(Object.class);
        final ListeningExecutorService dataModelExecutor = mock(ListeningExecutorService.class);
        when(dataModelExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object arg0 = invocation.getArguments()[0];
                final Callable<?> submittedCallable = (Callable<?>) arg0;
                final Object result = submittedCallable.call();
                return immediateFuture(result);
            }
        });

        final DataModelContext dataModelContext = mock(DataModelContext.class);
        final EventSignals eventSignals = mock(EventSignals.class);
        final EventSignal eventSignal = mock(EventSignal.class);
        final EventSignal[] eventSignalsValue = new EventSignal[]{eventSignal};
        when(eventSignals.value()).thenReturn(eventSignalsValue);
        final ObservableCollection observableCollection = mock(ObservableCollection.class);
        final PropertySlots propertySlots = mock(PropertySlots.class);
        final PropertySlot propertySlot = mock(PropertySlot.class);
        final PropertySlot[] propertySlotsValue = new PropertySlot[]{propertySlot};
        when(propertySlots.value()).thenReturn(propertySlotsValue);

        //root view binding meta
        final ViewBindingMeta rootViewBindingMeta = mock(ViewBindingMeta.class);
        when(rootViewBindingMeta.getViewModel()).thenReturn(viewModel);
        when(rootViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(dataModelContext));
        when(rootViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(rootViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(rootViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        //nested view binding meta
        final ViewBindingMeta nestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(nestedViewBindingMeta.getViewModel()).thenReturn(nestedViewModel);
        when(nestedViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(dataModelContext));
        when(nestedViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(nestedViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(nestedViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));
        when(nestedViewBindingMeta.hashCode()).thenReturn(123);

        //deep nested view binding meta
        final ViewBindingMeta deepNestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(deepNestedViewBindingMeta.getViewModel()).thenReturn(deepNestedViewModel);
        when(deepNestedViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(dataModelContext));
        when(deepNestedViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(deepNestedViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(deepNestedViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        //new nested view binding meta
        final ViewBindingMeta newNestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(newNestedViewBindingMeta.getViewModel()).thenReturn(newNestedViewModel);
        when(newNestedViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(dataModelContext));
        when(newNestedViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(newNestedViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(newNestedViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));
        when(newNestedViewBindingMeta.hashCode()).thenReturn(123);

        when(nestedViewBindingMeta.equals(newNestedViewBindingMeta)).thenReturn(TRUE);
        when(newNestedViewBindingMeta.equals(nestedViewBindingMeta)).thenReturn(TRUE);

        //root view binding meta creation
        mockStatic(ViewBindingMeta.class);
        when(ViewBindingMeta.create(dataModel, viewModel)).thenReturn(rootViewBindingMeta);

        //created root bindings
        final CollectionBinding collectionBinding = mock(CollectionBinding.class);
        final EventBinding eventBinding = mock(EventBinding.class);
        final PropertyBinding propertyBinding = mock(PropertyBinding.class);

        //created bindings for root view binding meta
        when(this.collectionBindingFactory.create(rootViewBindingMeta, observableCollection)).thenReturn(collectionBinding);
        when(this.eventBindingFactory.create(rootViewBindingMeta, eventSignal)).thenReturn(eventBinding);
        when(this.propertyBindingFactory.create(rootViewBindingMeta, propertySlot)).thenReturn(propertyBinding);

        //created nested bindings
        final CollectionBinding nestedCollectionBinding = mock(CollectionBinding.class);
        final EventBinding nestedEventBinding = mock(EventBinding.class);
        final PropertyBinding nestedPropertyBinding = mock(PropertyBinding.class);

        //created bindings for nested view binding meta
        when(this.collectionBindingFactory.create(nestedViewBindingMeta, observableCollection)).thenReturn(nestedCollectionBinding);
        when(this.eventBindingFactory.create(nestedViewBindingMeta, eventSignal)).thenReturn(nestedEventBinding);
        when(this.propertyBindingFactory.create(nestedViewBindingMeta, propertySlot)).thenReturn(nestedPropertyBinding);

        //created deep nested bindings
        final CollectionBinding deepNestedCollectionBinding = mock(CollectionBinding.class);
        final EventBinding deepNestedEventBinding = mock(EventBinding.class);
        final PropertyBinding deepNestedPropertyBinding = mock(PropertyBinding.class);

        //created bindings for deep nested view binding meta
        when(this.collectionBindingFactory.create(deepNestedViewBindingMeta, observableCollection)).thenReturn(deepNestedCollectionBinding);
        when(this.eventBindingFactory.create(deepNestedViewBindingMeta, eventSignal)).thenReturn(deepNestedEventBinding);
        when(this.propertyBindingFactory.create(deepNestedViewBindingMeta, propertySlot)).thenReturn(deepNestedPropertyBinding);

        //view binding meta discovery
        final FluentIterable<ViewBindingMeta> bindingMetas = FluentIterable.from(Arrays.asList(rootViewBindingMeta, nestedViewBindingMeta, deepNestedViewBindingMeta));

        when(this.viewBindingsTraverser.preOrderTraversal((ViewBindingMeta) any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                final Object[] args = invocation.getArguments();
                final ViewBindingMeta arg0 = (ViewBindingMeta) args[0];
                if (arg0 == rootViewBindingMeta) {
                    return bindingMetas;
                } else {
                    return FluentIterable.from(Arrays.asList());
                }
            }
        });

        //when
        //the nested view model is replaced

        //then
        //the old bindings are unbound
        //new bindings are created for the new nested view model

    }
}

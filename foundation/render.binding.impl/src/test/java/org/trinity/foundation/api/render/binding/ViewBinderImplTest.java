package org.trinity.foundation.api.render.binding;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.trinity.foundation.api.render.binding.view.DataModelContext;
import org.trinity.foundation.api.render.binding.view.EventSignals;
import org.trinity.foundation.api.render.binding.view.ObservableCollection;
import org.trinity.foundation.api.render.binding.view.PropertySlots;

import java.util.Arrays;
import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ViewBinderImplTest {

    @Mock
    private ViewBindingsTraverser viewBindingsTraverser;
    @InjectMocks
    private ViewBinderImpl2 viewBinderImpl;

    @Test
    public void testBind() {
        //given
        //a view binder
        //a root data model
        //a view model
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

        final ViewBindingMeta rootViewBindingMeta = mock(ViewBindingMeta.class);
        final DataModelContext dataModelContext = mock(DataModelContext.class);
        final EventSignals eventSignals = mock(EventSignals.class);
        final ObservableCollection observableCollection = mock(ObservableCollection.class);
        final PropertySlots propertySlots = mock(PropertySlots.class);

        when(rootViewBindingMeta.getViewModel()).thenReturn(viewModel);
        when(rootViewBindingMeta.getDataModelContext()).thenReturn(Optional.of(dataModelContext));
        when(rootViewBindingMeta.getEventSignals()).thenReturn(Optional.of(eventSignals));
        when(rootViewBindingMeta.getObservableCollection()).thenReturn(Optional.of(observableCollection));
        when(rootViewBindingMeta.getPropertySlots()).thenReturn(Optional.of(propertySlots));

        final ViewBindingMeta nestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(nestedViewBindingMeta.getViewModel()).thenReturn(nestedViewModel);

        final ViewBindingMeta deepNestedViewBindingMeta = mock(ViewBindingMeta.class);
        when(deepNestedViewBindingMeta.getViewModel()).thenReturn(deepNestedViewModel);

        final FluentIterable<ViewBindingMeta> viewBindingMetas = FluentIterable.from(Arrays.asList(rootViewBindingMeta, nestedViewBindingMeta, deepNestedViewBindingMeta));
        when(viewBindingsTraverser.preOrderTraversal((ViewBindingMeta) any())).thenReturn(viewBindingMetas);

        //when
        //the view model is bound to the data model
        this.viewBinderImpl.bind(dataModelExecutor, dataModel, viewModel);

        //then
        //all defined bindings in the view model are created and bound to the data model


    }

    @Test
    public void testUpdateDataModelBinding() {

    }

    @Test
    public void testUpdateViewModelBinding() {

    }
}

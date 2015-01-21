package org.trinity.foundation.api.binding.binding;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.binding.api.view.DataModelContext;
import org.trinity.binding.api.view.EventSignals;
import org.trinity.binding.api.view.ObservableCollection;
import org.trinity.binding.api.view.PropertySlots;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SubViewBindingMetaTest {

    public static class ViewModel {
        private ViewModel subViewModel;
    }

    public static class DataModel {
        public Object getFoo() {
            return this;
        }

        public Object getBar() {
            return this;
        }
    }

    @Mock
    private Object          viewModel;
    @Mock
    private ViewBindingMeta parentViewBindingMeta;

    private Field                          subViewField;
    @Mock
    private Optional<ObservableCollection> observableCollection;
    @Mock
    private Optional<DataModelContext>     dataModelContext;
    @Mock
    private Optional<EventSignals>         eventSignals;
    @Mock
    private Optional<PropertySlots>        propertySlot;


    @Before
    public void setUp() throws NoSuchFieldException {
        this.subViewField = ViewModel.class.getDeclaredField("subViewModel");
    }

    @Test
    public void testResolveDataModelChainNoDataContextSuccess() {
        //given
        //a sub view binding meta
        //a parent relative datacontext
        //no own relative dataContext
        when(this.parentViewBindingMeta.resolveDataModelChain((LinkedList<DataModelProperty>) any())).thenReturn(TRUE);
        final ViewBindingMeta subViewBindingMeta = SubViewBindingMeta.create(this.parentViewBindingMeta,
                                                                             this.subViewField,
                                                                             this.viewModel);

        //when
        //a data model context path is requested to be resolved
        final LinkedList<DataModelProperty> dataModelChain = new LinkedList<>();
        final DataModelProperty currentContext = mock(DataModelProperty.class);
        dataModelChain.add(currentContext);
        final boolean pathResolved = subViewBindingMeta.resolveDataModelChain(dataModelChain);

        //then
        //the path is correctly resolved
        assertTrue(pathResolved);
        assertEquals(1,
                     dataModelChain.size());
        assertEquals(currentContext,
                     dataModelChain.getFirst());
    }

    @Test
    public void testResolveDataModelChainWithDataContextSuccess() {
        //given
        //a sub view binding meta
        //a parent relative datacontext
        //a resolvable relative dataContext
        final DataModelContext dataModelContextValue = mock(DataModelContext.class);
        when(dataModelContextValue.value()).thenReturn("foo.bar");
        when(this.parentViewBindingMeta.resolveDataModelChain((LinkedList<DataModelProperty>) any())).thenReturn(TRUE);
        when(this.dataModelContext.isPresent()).thenReturn(TRUE);
        when(this.dataModelContext.get()).thenReturn(dataModelContextValue);

        final DataModelProperty currentContext = mock(DataModelProperty.class);
        final DataModel dataModel = spy(new DataModel());
        when(currentContext.getPropertyValue()).thenReturn(Optional.<Object>of(dataModel));

        final ViewBindingMeta subViewBindingMeta = SubViewBindingMeta.create(this.parentViewBindingMeta,
                                                                             this.subViewField,
                                                                             this.viewModel);

        //when
        //a data model context path is requested to be resolved
        final LinkedList<DataModelProperty> dataModelChain = new LinkedList<>();
        dataModelChain.add(currentContext);
        final boolean pathResolved = subViewBindingMeta.resolveDataModelChain(dataModelChain);

        //then
        //the path is correctly resolved
        assertTrue(pathResolved);
        assertEquals(3,
                     dataModelChain.size());
        assertEquals(currentContext,
                     dataModelChain.getFirst());

        verify(dataModel).getFoo();
    }

    @Test
    public void testResolveDataModelChainWithDataContextNoSuccess() {
        //given
        //a sub view binding meta
        //a parent relative datacontext
        //an unresolvable relative dataContext
        final DataModelContext dataModelContextValue = mock(DataModelContext.class);
        when(dataModelContextValue.value()).thenReturn("foo.bar.baz.qux");
        when(this.parentViewBindingMeta.resolveDataModelChain((LinkedList<DataModelProperty>) any())).thenReturn(TRUE);
        when(this.dataModelContext.isPresent()).thenReturn(TRUE);
        when(this.dataModelContext.get()).thenReturn(dataModelContextValue);

        final DataModelProperty currentContext = mock(DataModelProperty.class);
        final DataModel dataModel = spy(new DataModel());
        when(currentContext.getPropertyValue()).thenReturn(Optional.<Object>of(dataModel));

        final ViewBindingMeta subViewBindingMeta = SubViewBindingMeta.create(this.parentViewBindingMeta,
                                                                             this.subViewField,
                                                                             this.viewModel);


        //when
        //a data model context path is requested to be resolved
        final LinkedList<DataModelProperty> dataModelChain = new LinkedList<>();
        dataModelChain.add(currentContext);
        final boolean pathResolved = subViewBindingMeta.resolveDataModelChain(dataModelChain);

        //then
        //the path is resolved as far as possible
        assertFalse(pathResolved);
        assertEquals(4,
                     dataModelChain.size());
        assertEquals(currentContext,
                     dataModelChain.getFirst());
        verify(dataModel).getFoo();
        verify(dataModel).getBar();
    }

    @Test
    public void testHash() {
        //given
        //2 sub view binding metas, constructed with the same arguments
        final ViewBindingMeta subViewBindingMeta0 = SubViewBindingMeta.create(this.parentViewBindingMeta,
                                                                              this.subViewField,
                                                                              this.viewModel);

        final ViewBindingMeta subViewBindingMeta1 = SubViewBindingMeta.create(this.parentViewBindingMeta,
                                                                              this.subViewField,
                                                                              this.viewModel);

        //when
        //there hashes are calculated
        final int hash0 = subViewBindingMeta0.hashCode();
        final int hash1 = subViewBindingMeta1.hashCode();

        //then
        //there hashes are the same
        assertEquals(hash0,
                     hash1);
    }

    @Test
    public void testEquals() {
        //given
        //2 sub view binding metas, constructed with the same arguments
        final ViewBindingMeta subViewBindingMeta0 = SubViewBindingMeta.create(this.parentViewBindingMeta,
                                                                              this.subViewField,
                                                                              this.viewModel);

        final ViewBindingMeta subViewBindingMeta1 = SubViewBindingMeta.create(this.parentViewBindingMeta,
                                                                              this.subViewField,
                                                                              this.viewModel);

        //when
        //the sub view binding metas are checked for equality
        final boolean equals0 = subViewBindingMeta0.equals(subViewBindingMeta1);
        final boolean equals1 = subViewBindingMeta1.equals(subViewBindingMeta0);

        //then
        //they are considered equal (zomg socialism!)
        assertTrue(equals0);
        assertTrue(equals1);
    }
}

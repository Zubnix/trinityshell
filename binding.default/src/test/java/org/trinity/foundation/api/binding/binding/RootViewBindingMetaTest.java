package org.trinity.foundation.api.binding.binding;

import com.google.common.base.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.binding.api.view.DataModelContext;
import org.trinity.binding.api.view.EventSignals;
import org.trinity.binding.api.view.ObservableCollection;
import org.trinity.binding.api.view.PropertySlots;

import java.util.LinkedList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RootViewBindingMetaTest {

    public static class DataModel {
        public Object getFoo() {
            return this;
        }

        public Object getBar() {
            return this;
        }
    }

    @Mock
    private Object                         viewModel;
    @Spy
    private DataModel                      dataModel;
    @Mock
    private Optional<ObservableCollection> observableCollection;
    @Mock
    private Optional<DataModelContext>     dataModelContext;
    @Mock
    private Optional<EventSignals>         eventSignals;
    @Mock
    private Optional<PropertySlots>        propertySlots;
    //@InjectMocks for some reason mockito is totally confused as what to inject so we build the object manually
    private ViewBindingMeta                rootViewBindingMeta;

    @Test
    public void testResolveDataModelChainNoDataContextSuccess() {
        //given
        //a root view binding meta with no relative dataContext
        when(this.dataModelContext.isPresent()).thenReturn(FALSE);

        this.rootViewBindingMeta = RootViewBindingMeta.create(this.viewModel,
                                                              this.dataModel);

        //when
        //a data model context path is requested to be resolved
        final LinkedList<DataModelProperty> dataModelChain = new LinkedList<>();
        final boolean pathResolved = this.rootViewBindingMeta.resolveDataModelChain(dataModelChain);

        //then
        //the path is correctly resolved
        assertTrue(pathResolved);
        assertEquals(1,
                     dataModelChain.size());
        assertEquals(this.dataModel,
                     dataModelChain.getFirst()
                                   .getPropertyValue()
                                   .get());
    }


    @Test
    public void testResolveDataModelChainWithDataContextSuccess() {
        //given
        //a root view binding meta with relative resolvable dataContext
        final DataModelContext dataModelContextValue = mock(DataModelContext.class);
        when(dataModelContextValue.value()).thenReturn("foo.bar");
        when(this.dataModelContext.isPresent()).thenReturn(TRUE);
        when(this.dataModelContext.get()).thenReturn(dataModelContextValue);

        this.rootViewBindingMeta = RootViewBindingMeta.create(this.viewModel,
                                                              this.dataModel);

        //when
        //a data model context path is requested to be resolved
        final LinkedList<DataModelProperty> dataModelChain = new LinkedList<>();
        final boolean pathResolved = this.rootViewBindingMeta.resolveDataModelChain(dataModelChain);

        //then
        //the path is correctly resolved
        assertTrue(pathResolved);
        assertEquals(3,
                     dataModelChain.size());
        assertEquals(this.dataModel,
                     dataModelChain.getFirst()
                                   .getPropertyValue()
                                   .get());

        verify(this.dataModel).getFoo();
    }

    @Test
    public void testResolveDataModelChainWithDataContextNoSuccess() {
        //given
        //a root view binding meta with relative unresolvable dataContext
        final DataModelContext dataModelContextValue = mock(DataModelContext.class);
        when(dataModelContextValue.value()).thenReturn("foo.bar.baz.qux");
        when(this.dataModelContext.isPresent()).thenReturn(TRUE);
        when(this.dataModelContext.get()).thenReturn(dataModelContextValue);

        this.rootViewBindingMeta = RootViewBindingMeta.create(this.viewModel,
                                                              this.dataModel);

        //when
        // a data model context path is requested to be resolved
        final LinkedList<DataModelProperty> dataModelChain = new LinkedList<>();
        final boolean pathResolved = this.rootViewBindingMeta.resolveDataModelChain(dataModelChain);

        //then
        //the path is not completely resolved
        assertFalse(pathResolved);
        assertEquals(4,
                     dataModelChain.size());
        assertEquals(this.dataModel,
                     dataModelChain.getFirst()
                                   .getPropertyValue()
                                   .get());

        verify(this.dataModel).getFoo();
        verify(this.dataModel).getBar();
    }

    @Test
    public void testHash() {
        //given
        //2 root view binding metas constructed with the same data model and view model
        final ViewBindingMeta rootViewBindingMeta0 = RootViewBindingMeta.create(this.viewModel,
                                                                                this.dataModel);

        final ViewBindingMeta rootViewBindingMeta1 = RootViewBindingMeta.create(this.viewModel,
                                                                                this.dataModel);

        //when
        //there hashes are calculated
        final int hashCode0 = rootViewBindingMeta0.hashCode();
        final int hashCode1 = rootViewBindingMeta1.hashCode();

        //then
        //there hashes are the same
        assertEquals(hashCode0,
                     hashCode1);
    }

    @Test
    public void testEquals() {
        //given
        //2 root view binding metas constructed with the same data model and view model
        final ViewBindingMeta rootViewBindingMeta0 = RootViewBindingMeta.create(this.viewModel,
                                                                                this.dataModel);

        final ViewBindingMeta rootViewBindingMeta1 = RootViewBindingMeta.create(this.viewModel,
                                                                                this.dataModel);

        //when
        //they are compared for equality
        final boolean equals0 = rootViewBindingMeta0.equals(rootViewBindingMeta1);
        final boolean equals1 = rootViewBindingMeta1.equals(rootViewBindingMeta0);


        //then
        //they are considered the same
        assertTrue(equals0);
        assertTrue(equals1);
    }
}

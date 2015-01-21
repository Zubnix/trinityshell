package org.trinity.foundation.api.binding.binding;

import dagger.ObjectGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.binding.api.view.PropertyAdapter;
import org.trinity.binding.api.view.PropertySlot;
import org.trinity.binding.api.view.delegate.PropertySlotInvocationDelegate;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PropertyBindingTest {

    //mocks needed for view binding meta mock
    @Mock
    private PropertyAdapter propertyAdapter;
    @Spy
    private final Object viewModel = new Object() {
        public void qux(final int arg0,
                        final String arg1) {
        }
    };
    @Mock
    private Object fooValue;

    private final Object bazValue = new Object();
    @Spy
    private final Object barValue = new Object() {
        public Object getBaz() {
            return PropertyBindingTest.this.bazValue;
        }
    };

    //mocks needed for property slot mock
    private final String  propertySlotDataContext  = "foo.bar";
    private final String  propertyName             = "baz";
    private final String  propertySlotMethodName   = "qux";
    private final Class[] propertySlotArgumentTyps = new Class[]{int.class,
            String.class};
    private final int     arg0Val                  = 123;
    private final String  arg1Val                  = "456";

    //mocks needed for property binding
    @Mock
    private ObjectGraph                    injector;
    @Mock
    private PropertySlotInvocationDelegate propertySlotInvocationDelegate;
    @Mock
    private ViewBindingMeta                viewBindingMeta;
    @Mock
    private PropertySlot                   propertySlot;

    @InjectMocks
    private PropertyBinding propertyBinding;

    @Before
    public void setUp() {

        when(this.propertyAdapter.adapt(this.bazValue)).thenReturn(new Object[]{this.arg0Val,
                this.arg1Val});

        when(this.injector.get(PropertyAdapter.class)).thenReturn(this.propertyAdapter);
        when(this.viewBindingMeta.getViewModel()).thenReturn(this.viewModel);
        when(this.propertySlot.dataModelContext()).thenReturn(this.propertySlotDataContext);
        when(this.propertySlot.propertyName()).thenReturn(this.propertyName);
        when(this.propertySlot.methodName()).thenReturn(this.propertySlotMethodName);
        when(this.propertySlot.argumentTypes()).thenReturn(this.propertySlotArgumentTyps);
        when(this.propertySlot.adapter()).thenReturn((Class) PropertyAdapter.class);
    }

    @Test
    public void testBindResolvableDataModelContext() throws NoSuchMethodException {
        //given
        //a property binding
        //a fully resolvable data model context
        when(this.viewBindingMeta.resolveDataModelChain((LinkedList<DataModelProperty>) any())).thenReturn(TRUE);
        when(this.viewBindingMeta.appendDataModelPropertyChain((LinkedList<DataModelProperty>) any(),
                                                               eq(this.propertySlotDataContext))).thenAnswer(invocation -> {
            final Object[] args = invocation.getArguments();
            final LinkedList<DataModelProperty> arg0 = (LinkedList<DataModelProperty>) args[0];
            arg0.add(ConstantDataModelProperty.create(PropertyBindingTest.this.fooValue));
            arg0.add(ConstantDataModelProperty.create(PropertyBindingTest.this.barValue));
            return TRUE;
        });

        //when
        //a bind is performed
        final Collection<DataModelProperty> dataModelProperties = this.propertyBinding.bind();

        //then
        //the view model is updated with the data model property value
        //the fully resolved data model properties chain is returned
        assertEquals(3,
                     dataModelProperties.size());
        final Method viewMethod = this.viewModel.getClass()
                                                .getMethod("qux",
                                                           this.propertySlotArgumentTyps);

        verify(this.propertySlotInvocationDelegate).invoke(eq(this.viewModel),
                                                           eq(viewMethod),
                                                           aryEq(new Object[]{this.arg0Val,
                                                                   this.arg1Val}));
    }

    @Test
    public void testBindUnresolvableDataModelContext() throws NoSuchMethodException {
        //given
        //a property binding
        //an unresolvable data model context
        when(this.viewBindingMeta.resolveDataModelChain((LinkedList<DataModelProperty>) any())).thenReturn(TRUE);
        when(this.viewBindingMeta.appendDataModelPropertyChain((LinkedList<DataModelProperty>) any(),
                                                               eq(this.propertySlotDataContext))).thenAnswer(invocation -> {
            final Object[] args = invocation.getArguments();
            final LinkedList<DataModelProperty> arg0 = (LinkedList<DataModelProperty>) args[0];
            arg0.add(ConstantDataModelProperty.create(PropertyBindingTest.this.fooValue));
            return FALSE;
        });
        //when
        //a bind is performed
        final Collection<DataModelProperty> dataModelProperties = this.propertyBinding.bind();

        //then
        //the view model is not updated
        //the data model properties that could be resolved are returned
        assertEquals(1,
                     dataModelProperties.size());
        final Method viewMethod = this.viewModel.getClass()
                                                .getMethod("qux",
                                                           this.propertySlotArgumentTyps);

        verify(this.propertySlotInvocationDelegate,
               never()).invoke(eq(this.viewModel),
                               eq(viewMethod),
                               aryEq(new Object[]{this.arg0Val,
                                       this.arg1Val}));
    }

    @Test
    public void testUnbind() throws NoSuchMethodException {
        //given
        //a property binding

        //when
        //the property binding is unbound
        this.propertyBinding.unbind();

        //then
        //nothing happens because property binding is stateless :)
        final Method viewMethod = this.viewModel.getClass()
                                                .getMethod("qux",
                                                           this.propertySlotArgumentTyps);

        verify(this.propertySlotInvocationDelegate,
               never()).invoke(eq(this.viewModel),
                               eq(viewMethod),
                               aryEq(new Object[]{this.arg0Val,
                                       this.arg1Val}));
    }
}

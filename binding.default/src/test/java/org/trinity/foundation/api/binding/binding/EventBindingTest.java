package org.trinity.foundation.api.binding.binding;

import dagger.ObjectGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.trinity.binding.api.view.EventSignal;
import org.trinity.binding.api.view.EventSignalFilter;

import java.util.LinkedList;

import static java.lang.Boolean.TRUE;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventBindingTest {

    @Mock
    private Object viewModel;
    @Mock
    private Object dataModel;

    @Mock
    private EventSignalFilter eventSignalFilter;
    @Mock
    private SignalDefault     signal;
    private final String signalMethodName = "onFoo";

    @Mock
    private ObjectGraph     injector;
    @Mock
    private SignalFactory   signalFactor;
    @Mock
    private ViewBindingMeta viewBindingMeta;
    @Mock
    private EventSignal     eventSignal;
    @InjectMocks
    private EventBinding    eventBinding;

    @Before
    public void setUp() {
        when(this.viewBindingMeta.resolveDataModelChain((LinkedList<DataModelProperty>) any())).thenAnswer(invocation -> {
            final Object arg0 = invocation.getArguments()[0];
            final LinkedList<DataModelProperty> properties = (LinkedList<DataModelProperty>) arg0;
            properties.add(ConstantDataModelProperty.create(EventBindingTest.this.dataModel));
            return TRUE;
        });
        when(this.viewBindingMeta.appendDataModelPropertyChain((LinkedList<DataModelProperty>) any(),
                                                               (String) any())).thenReturn(TRUE);

        when(this.eventSignal.filter()).thenReturn((Class) EventSignal.class);
        when(this.eventSignal.name()).thenReturn(this.signalMethodName);

        when(this.injector.get((Class) EventSignal.class)).thenReturn(this.eventSignalFilter);

        when(this.signalFactor.create(
                this.dataModel,
                this.signalMethodName)).thenReturn(this.signal);
    }

    @Test
    public void testBind() {
        //given
        //a data model
        //a view model with events
        //an event binding

        //when
        //an event binding is bound
        this.eventBinding.bind();

        //then
        //the view model is listened for events
        this.eventSignalFilter.installFilter(this.viewModel,
                                             this.signal);

    }

    @Test
    public void testUnbind() {
        //given
        //a data model
        //a view model with events
        //an bound event binding
        this.eventBinding.bind();

        //when
        //an event binding is unbound
        this.eventBinding.unbind();

        //then
        //the view model is no longer listened for events
        this.eventSignalFilter.uninstallFilter(this.viewModel,
                                               this.signal);
    }
}

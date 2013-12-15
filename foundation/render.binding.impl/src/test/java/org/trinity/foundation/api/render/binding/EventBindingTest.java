package org.trinity.foundation.api.render.binding;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.trinity.foundation.api.render.binding.view.EventSignal;
import org.trinity.foundation.api.render.binding.view.EventSignalFilter;
import org.trinity.foundation.api.render.binding.view.delegate.Signal;

import java.util.LinkedList;
import java.util.concurrent.Callable;

import static com.google.common.util.concurrent.Futures.immediateFuture;
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
	private Signal            signal;
	private final String signalMethodName = "onFoo";

	@Mock
	private Injector                 injector;
	@Mock
	private SignalFactory            signalFactor;
	@Mock
	private ListeningExecutorService dataModelExecutor;
	@Mock
	private ViewBindingMeta          viewBindingMeta;
	@Mock
	private EventSignal              eventSignal;
	@InjectMocks
	private EventBinding             eventBinding;

	@Before
	public void setUp() {
		when(this.dataModelExecutor.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object arg0 = invocation.getArguments()[0];
				final Callable<?> submittedCallable = (Callable<?>) arg0;
				final Object result = submittedCallable.call();
				return immediateFuture(result);
			}
		});

		when(this.viewBindingMeta.resolveDataModelChain((LinkedList<DataModelProperty>) any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object arg0 = invocation.getArguments()[0];
				final LinkedList<DataModelProperty> properties = (LinkedList<DataModelProperty>) arg0;
				properties.add(new ConstantDataModelProperty(EventBindingTest.this.dataModel));
				return TRUE;
			}
		});
		when(this.viewBindingMeta.appendDataModelPropertyChain((LinkedList<DataModelProperty>) any(),
															   (String) any())).thenReturn(TRUE);

		when(this.eventSignal.filter()).thenReturn((Class) EventSignal.class);
		when(this.eventSignal.name()).thenReturn(this.signalMethodName);

		when(this.injector.getInstance((Class) EventSignal.class)).thenReturn(this.eventSignalFilter);

		when(this.signalFactor.createSignal(this.dataModelExecutor,
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

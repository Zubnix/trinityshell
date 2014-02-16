package org.trinity.foundation.api.shared;

import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.Futures.immediateFuture;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MoreExecutors.class)
public class ListenableEventBusTest {

	public static class Listener {
		@Subscribe
		public void handleEvent(final Object event) {

		}
	}

	@Spy
	public Listener listener = new Listener();

	@Mock
	private ExecutorService         postingExecutorService;
	@InjectMocks
	private ListenableEventBus listenableEventBus;

	@Before
	public void setUp() {
		when(this.postingExecutorService.submit(Matchers.<Callable>any())).thenAnswer(new Answer<Object>() {
			@Override
			public Object answer(final InvocationOnMock invocation) throws Throwable {
				final Object arg0 = invocation.getArguments()[0];
				final Callable<?> submittedCallable = (Callable<?>) arg0;
				final Object result = submittedCallable.call();
				return immediateFuture(result);
			}
		});
	}

	@Test
	public void testRegisterWithSameExecutorActivator() {
		//given
		//an async listanble eventbus
		//a listener

		//when
		//a listener is registered
		//an event is posted
		final Object event = new Object();
		this.listenableEventBus.register(this.listener);
		this.listenableEventBus.post(event);

		//then
		//the listener is registered by the thread that performs the register call
		//the listener's event handle method is called by the eventbus' postingExecutor
		//FIXME how to verify which executor service does what action?
		verify(this.postingExecutorService,
			   times(1)).submit((Callable<Object>) any());
		verify(this.listener).handleEvent(event);
	}

	@Test
	public void testRegisterWithOtherExecutorActivator() {
		//given
		//an async listanble eventbus
		//a listener
		//an event handler executor service
		final ExecutorService eventHandlerExecutorService = mock(ExecutorService.class);

		//when
		//a listener is registered with it's own event handler executor service.
		//an event is posted
		final Object event = new Object();
		this.listenableEventBus.register(this.listener,
											  eventHandlerExecutorService);
		this.listenableEventBus.post(event);

		//then
		//the listener is registered by the thread that performs the register call
		//the listener's event handle method is called by it's own executor service.
		//FIXME how to verify which executor service does what action?
		verify(this.postingExecutorService,
			   times(1)).submit((Callable<Object>) any());
		verify(eventHandlerExecutorService).execute((Runnable) any());
	}

	@Test
	public void testScheduleRegisterWithSameExecutorActivator() {
		//given
		//an async listanble eventbus
		//a listener

		//when
		//a listener is registered
		//an event is posted
		final Object event = new Object();
		this.listenableEventBus.scheduleRegister(this.listener);
		this.listenableEventBus.post(event);

		//then
		//the listener is registered by the eventbus' executor service
		//the listener's event handle method is called by the eventbus' postingExecutor
		//FIXME how to verify which executor service does what action?
		verify(this.postingExecutorService,
			   times(2)).submit((Callable<Object>) any());
		verify(this.listener).handleEvent(event);
	}

	@Test
	public void testScheduleRegisterWithOtherExecutorActivator() {
		//given
		//an async listanble eventbus
		//a listener
		//an event handler executor service
		final ExecutorService eventHandlerExecutorService = mock(ExecutorService.class);

		//when
		//the listener is registered by the eventbus' executor service
		//an event is posted
		final Object event = new Object();
		this.listenableEventBus.scheduleRegister(this.listener,
													  eventHandlerExecutorService);
		this.listenableEventBus.post(event);

		//then
		//the listener is registered by the eventbus' executor service
		//the listener's event handle method is called by it's own executor service.
		//FIXME how to verify which executor service does what action?
		verify(this.postingExecutorService,
			   times(2)).submit((Callable<Object>) any());
		verify(eventHandlerExecutorService).execute((Runnable) any());
	}

	@Test
	public void testUnregister() {
		//given
		//an async listanble eventbus
		//differently registered listeners
		final Listener listener0 = spy(new Listener());

		final ExecutorService eventHandlerExecutorService1 = mock(ExecutorService.class);
		final Listener listener1 = spy(new Listener());

		final Listener listener2 = spy(new Listener());

		final ExecutorService eventHandlerExecutorService3 = mock(ExecutorService.class);
		final Listener listener3 = spy(new Listener());

		this.listenableEventBus.register(listener0);
		this.listenableEventBus.register(listener1,
											  eventHandlerExecutorService1);
		this.listenableEventBus.scheduleRegister(listener2);
		this.listenableEventBus.scheduleRegister(listener3,
													  eventHandlerExecutorService3);

		//when
		//all listeners are unregistered
		//an event is posted
		this.listenableEventBus.unregister(listener0);
		this.listenableEventBus.unregister(listener1);
		this.listenableEventBus.unregister(listener2);
		this.listenableEventBus.unregister(listener3);

		final Object event = new Object();
		this.listenableEventBus.post(event);

		//then
		//no event will reach the listeners
		verify(listener0,never()).handleEvent(event);
		verify(listener1,never()).handleEvent(event);
		verify(listener2,never()).handleEvent(event);
		verify(listener3,never()).handleEvent(event);
	}
}
